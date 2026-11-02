package com.backend.gestao_ong_animais_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requsição para autenticação de usuário")
public record AuthRequestDTO(
        @Schema(description = "E-mail do usuário", example = "admin@ong.com.br")
        @NotBlank(message = "E-mail é obrigatório")
        @Email
        String email,

        @Schema(description = "Senha do usuário", example = "senha123")
        @NotBlank(message = "Senha é obrigatória")
        String senha
) {}
