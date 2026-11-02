package com.backend.gestao_ong_animais_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Objeto de requisição exclusivo para alteração de senha")
public record UsuarioSenhaRequestDTO(
        @Schema(description = "Senha atual para validação de segurança", example = "SenhaSegura123")
        @NotBlank(message = "A senha atual é obrigatória")
        String senhaAtual,

        @Schema(description = "Nova senha de acesso", example = "NovaSenhaOng456")
        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
        String novaSenha
) {}
