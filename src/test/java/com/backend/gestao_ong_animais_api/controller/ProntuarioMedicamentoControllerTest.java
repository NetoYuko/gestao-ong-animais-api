package com.backend.gestao_ong_animais_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.gestao_ong_animais_api.dto.AnimalRequestDTO;
import com.backend.gestao_ong_animais_api.dto.ProntuarioRequestDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProntuarioMedicamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Test
    @DisplayName("Deve registrar um medicamento para um animal existente com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveRegistrarMedicamento() throws Exception {
        AnimalRequestDTO animalDto = new AnimalRequestDTO("Bolinha", "GATO", 12, "DISPONIVEL", "https://fakeurl.com/foto-teste.jpg");
        String animalResponse = mockMvc.perform(post("/api/animais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(animalDto)))
                .andReturn().getResponse().getContentAsString();

        String animalIdStr = objectMapper.readTree(animalResponse).get("id").asText();
        UUID animalId = UUID.fromString(animalIdStr);

        ProntuarioRequestDTO prontuarioDto = new ProntuarioRequestDTO(
                animalId,
                "Bravecto",
                "ANTIPULGAS",
                LocalDate.now(),
                LocalDate.now().plusMonths(3),
                "Sem reações adversas"
        );

        mockMvc.perform(post("/api/prontuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(prontuarioDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.animalId").value(animalIdStr))
                .andExpect(jsonPath("$.nomeMedicamento").value("Bravecto"));
    }

    @Test
    @DisplayName("Deve consultar o histórico do animal com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarHistorico() throws Exception {
        mockMvc.perform(get("/api/prontuarios/animal/" + UUID.randomUUID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve consultar a listagem geral de prontuários com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarTodosOsProntuarios() throws Exception {
        mockMvc.perform(get("/api/prontuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}
