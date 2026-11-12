package com.backend.gestao_ong_animais_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.gestao_ong_animais_api.dto.AnimalRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AnimalControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve cadastrar um animal com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCadastrarAnimal() throws Exception {
        AnimalRequestDTO dto = new AnimalRequestDTO("Bolinha", "GATO", 12, "DISPONIVEL", "https://fakeurl.com/foto-teste.jpg");

        mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Bolinha"));
    }

    @Test
    @DisplayName("Deve listar animais com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarAnimais() throws Exception {
        mockMvc.perform(get("/api/animais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve atualizar dados de um animal sem permitir alteração direta de status (Permissão ADMIN)")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void deveAtualizarAnimal() throws Exception {
        AnimalRequestDTO criacaoDto = new AnimalRequestDTO("Rex", "CACHORRO", 5, "DISPONIVEL", null);
        String responseJson = mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto)))
                .andReturn().getResponse().getContentAsString();

        String idGerado = objectMapper.readTree(responseJson).get("id").asText();

        AnimalRequestDTO atualizacaoDto = new AnimalRequestDTO("Rex Editado", "CACHORRO", 6, "ADOTADO", null);

        mockMvc.perform(put("/api/animais/" + idGerado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacaoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Rex Editado"))
                .andExpect(jsonPath("$.idadeMeses").value(6))
                .andExpect(jsonPath("$.status").value("DISPONIVEL"));
    }

    @Test
    @DisplayName("Deve excluir um animal com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveExcluirAnimal() throws Exception {
        AnimalRequestDTO criacaoDto = new AnimalRequestDTO("Bolinha", "GATO", 5, "DISPONIVEL", null);
        String responseJson = mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto)))
                .andReturn().getResponse().getContentAsString();

        String idGerado = objectMapper.readTree(responseJson).get("id").asText();

        mockMvc.perform(delete("/api/animais/" + idGerado))
                .andExpect(status().isNoContent());
    }
}
