package com.backend.gestao_ong_animais_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.gestao_ong_animais_api.dto.AdocaoRequestDTO;
import com.backend.gestao_ong_animais_api.dto.AnimalRequestDTO;
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

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdocaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    @DisplayName("Deve aprovar uma adoção e atualizar o status do animal para ADOTADO automaticamente")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void deveRegistrarAprovacaoEAtualizarAnimal() throws Exception {
        AnimalRequestDTO animalDto = new AnimalRequestDTO("Bolinha", "GATO", 5, "DISPONIVEL", null);
        String animalResponse = mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(animalDto)))
                .andReturn().getResponse().getContentAsString();
        String animalIdStr = objectMapper.readTree(animalResponse).get("id").asText();
        UUID animalId = UUID.fromString(animalIdStr);

        TutorRequestDTO tutorDto = new TutorRequestDTO("João Silva", "555.666.777-88", "(11) 98888-7777", "Rua do Adotante, 45");
        String tutorResponse = mockMvc.perform(post("/api/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorDto)))
                .andReturn().getResponse().getContentAsString();
        String tutorIdStr = objectMapper.readTree(tutorResponse).get("id").asText();
        UUID tutorId = UUID.fromString(tutorIdStr);

        AdocaoRequestDTO adocaoDto = new AdocaoRequestDTO(animalId, tutorId, "APROVADO");
        mockMvc.perform(post("/api/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adocaoDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APROVADO"));


        mockMvc.perform(get("/api/animais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == '" + animalIdStr + "')].status").value("ADOTADO"));
    }

    @Test
    @DisplayName("Deve listar o histórico de processos de um animal com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarHistoricoDeAdocoes() throws Exception {
        mockMvc.perform(get("/api/adocoes/animal/" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve listar todas as adoções com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarTodasAdocoes() throws Exception {
        mockMvc.perform(get("/api/adocoes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve atualizar uma adoção existente para APROVADO e atualizar o animal (Permissão ADMIN)")
    @WithMockUser(roles = {"ADMIN", "USER"})
    void deveAtualizarAdocaoEAtualizarAnimal() throws Exception {
        AnimalRequestDTO animalDto = new AnimalRequestDTO("Rex", "CACHORRO", 3, "DISPONIVEL", null);
        String animalResponse = mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(animalDto)))
                .andReturn().getResponse().getContentAsString();
        String animalIdStr = objectMapper.readTree(animalResponse).get("id").asText();
        UUID animalId = UUID.fromString(animalIdStr);

        TutorRequestDTO tutorDto = new TutorRequestDTO("Carlos", "111.000.222-33", "(11) 90000-0000", "Rua Nova, 12");
        String tutorResponse = mockMvc.perform(post("/api/tutores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tutorDto)))
                .andReturn().getResponse().getContentAsString();
        String tutorIdStr = objectMapper.readTree(tutorResponse).get("id").asText();
        UUID tutorId = UUID.fromString(tutorIdStr);

        AdocaoRequestDTO adocaoCriacaoDto = new AdocaoRequestDTO(animalId, tutorId, "ANALISE");
        String adocaoResponse = mockMvc.perform(post("/api/adocoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adocaoCriacaoDto)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        String adocaoIdStr = objectMapper.readTree(adocaoResponse).get("id").asText();

        AdocaoRequestDTO adocaoAtualizacaoDto = new AdocaoRequestDTO(animalId, tutorId, "APROVADO");
        mockMvc.perform(put("/api/adocoes/" + adocaoIdStr)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adocaoAtualizacaoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APROVADO"));

        mockMvc.perform(get("/api/animais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == '" + animalIdStr + "')].status").value("ADOTADO"));
    }
}
