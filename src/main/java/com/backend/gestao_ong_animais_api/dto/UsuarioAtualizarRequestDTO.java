package com.backend.gestao_ong_animais_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

@Schema(description = "Objeto de requisição para atualização de dados cadastrais do usuário (sem envolver senha)")
public record UsuarioAtualizarRequestDTO(
        @Schema(description = "Novo nome do usuário", example = "Ana Silva Santos")
        String nome,

        @Schema(description = "Novo e-mail de acesso", example = "ana_novo@ong.com.br")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @Schema(description = "Novo perfil de acesso", example = "ADMIN")
        String perfil
) {}
