package com.backend.gestao_ong_animais_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para cadastro de um novo usuário no sistema")
public record UsuarioRequestDTO(
        @Schema(description = "Nome completo do usuário", example = "Ana Silva dos Santos")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(description = "E-mail de acesso", example = "ana@ong.com.br")
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,

        @Schema(description = "Senha de acesso", example = "SenhaSegura123")
        @NotBlank(message = "A senha é obrigatória")
        String senha,

        @Schema(description = "Perfil de acesso (VISUALIZADOR ou ADMIN)", example = "VISUALIZADOR")
        @NotBlank(message = "O perfil é obrigatório")
        String perfil
) {}
