package com.backend.gestao_ong_animais_api.dto;

import com.backend.gestao_ong_animais_api.model.Tutor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Objeto de resposta com os dados do tutor")
public record TutorResponseDTO(
        @Schema(description = "ID único do tutor", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @Schema(description = "Nome completo do tutor", example = "João da silva")
        String nome,
        @Schema(description = "CPF do tutor", example = "123.456.789-00")
        String cpf,
        @Schema(description = "Telefone de contato", example = "(82) 99999-9999")
        String telefone,
        @Schema(description = "Endereço completo", example = "Rua das Flores, 123, Bairro Centro")
        String endereco
) {
    public TutorResponseDTO(Tutor tutor) {
        this(tutor.getId(), tutor.getNome(), tutor.getCpf(), tutor.getTelefone(), tutor.getEndereco());
    }
}
