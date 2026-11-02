package com.backend.gestao_ong_animais_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.gestao_ong_animais_api.dto.TutorRequestDTO;
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
class TutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve cadastrar um tutor com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCadastrarTutor() throws Exception {
        TutorRequestDTO dto = new TutorRequestDTO("Maria da Silva", "111.222.333-44", "(82) 99999-9999", "Rua do Sol, 123");

        mockMvc.perform(post("/api/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Maria da Silva"))
                .andExpect(jsonPath("$.cpf").value("111.222.333-44"));
    }

    @Test
    @DisplayName("Deve listar tutores com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarTutores() throws Exception {
        mockMvc.perform(get("/api/tutores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve atualizar os dados do tutor com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarTutor() throws Exception {
        TutorRequestDTO criacaoDto = new TutorRequestDTO("Carlos Santos", "555.444.333-22", "(82) 98888-8888", "Rua Antiga, 10");
        String responseJson = mockMvc.perform(post("/api/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto)))
                .andReturn().getResponse().getContentAsString();

        String idGerado = objectMapper.readTree(responseJson).get("id").asText();

        TutorRequestDTO atualizacaoDto = new TutorRequestDTO("Carlos Santos", "555.444.333-22", "(82) 97777-7777", "Rua Nova, 20");

        mockMvc.perform(put("/api/tutores/" + idGerado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacaoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telefone").value("(82) 97777-7777"))
                .andExpect(jsonPath("$.endereco").value("Rua Nova, 20"));
    }

    @Test
    @DisplayName("Deve excluir um tutor com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveExcluirTutor() throws Exception {
        TutorRequestDTO criacaoDto = new TutorRequestDTO("Ana Lima", "999.888.777-66", "(82) 96666-6666", "Avenida Central, 50");
        String responseJson = mockMvc.perform(post("/api/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto)))
                .andReturn().getResponse().getContentAsString();

        String idGerado = objectMapper.readTree(responseJson).get("id").asText();

        mockMvc.perform(delete("/api/tutores/" + idGerado))
                .andExpect(status().isNoContent());
    }
}
