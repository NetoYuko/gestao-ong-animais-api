package com.backend.gestao_ong_animais_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.backend.gestao_ong_animais_api.dto.UsuarioAtualizarRequestDTO;
import com.backend.gestao_ong_animais_api.dto.UsuarioRequestDTO;
import com.backend.gestao_ong_animais_api.dto.UsuarioSenhaRequestDTO;
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
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveCadastrarUsuario() throws Exception {
        UsuarioRequestDTO dto = new UsuarioRequestDTO("Carlos", "carlos_admin@ong.com", "senha123", "ADMIN");

        mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Carlos"));
    }

    @Test
    @DisplayName("Deve listar usuários com sucesso")
    @WithMockUser(roles = "USER")
    void deveListarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    @DisplayName("Deve atualizar os dados básicos do usuário")
    @WithMockUser(roles = "ADMIN")
    void deveAtualizarDadosBasicosUsuario() throws Exception {
        UsuarioRequestDTO criacaoDto = new UsuarioRequestDTO("Marcos", "marcos_upd@ong.com", "senha123", "USER");
        String responseJson = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto)))
                .andReturn().getResponse().getContentAsString();

        String idGerado = objectMapper.readTree(responseJson).get("id").asText();

        UsuarioAtualizarRequestDTO atualizacaoDto = new UsuarioAtualizarRequestDTO("Marcos Silva", null, "ADMIN");

        mockMvc.perform(put("/api/usuarios/" + idGerado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atualizacaoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Marcos Silva"))
                .andExpect(jsonPath("$.perfil").value("ADMIN"));
    }

    @Test
    @DisplayName("Deve alterar a senha do usuário com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveAlterarSenha() throws Exception {
        UsuarioRequestDTO criacaoDto = new UsuarioRequestDTO("Joana", "joana_senha@ong.com", "senhaAntiga", "USER");
        String responseJson = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto)))
                .andReturn().getResponse().getContentAsString();

        String idGerado = objectMapper.readTree(responseJson).get("id").asText();

        UsuarioSenhaRequestDTO senhaDto = new UsuarioSenhaRequestDTO("senhaAntiga", "novaSenhaSegura");

        mockMvc.perform(patch("/api/usuarios/" + idGerado + "/senha")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(senhaDto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve excluir um usuário com sucesso")
    @WithMockUser(roles = "ADMIN")
    void deveExcluirUsuario() throws Exception {
        UsuarioRequestDTO criacaoDto = new UsuarioRequestDTO("Pedro", "pedro_del@ong.com", "senha123", "USER");
        String responseJson = mockMvc.perform(post("/api/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(criacaoDto)))
                .andReturn().getResponse().getContentAsString();

        String idGerado = objectMapper.readTree(responseJson).get("id").asText();

        mockMvc.perform(delete("/api/usuarios/" + idGerado))
                .andExpect(status().isNoContent());
    }
}
