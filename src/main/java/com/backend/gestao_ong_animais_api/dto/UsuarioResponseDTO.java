package com.backend.gestao_ong_animais_api.dto;

import com.backend.gestao_ong_animais_api.model.Usuario;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Objeto de resposta com os dados públicos do usuário (a senha nunca é retornada)")
public record UsuarioResponseDTO(
        @Schema(description = "ID único do usuário", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,
        @Schema(description = "Nome do usuário", example = "Ana Silva")
        String nome,
        @Schema(description = "E-mail de acesso", example = "ana@ong.com.br")
        String email,
        @Schema(description = "Perfil de acesso", example = "VISUALIZADOR")
        String perfil
) {
    public UsuarioResponseDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getPerfil());
    }
}