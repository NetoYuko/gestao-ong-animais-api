package com.backend.gestao_ong_animais_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Objeto de requisição para iniciar ou atualizar um processo de adoção")
public record AdocaoRequestDTO(
        @Schema(description = "ID do animal em processo de adoção", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "O ID do animal é obrigatório")
        UUID animalId,

        @Schema(description = "ID do tutor interessado", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotNull(message = "O ID do tutor é obrigatório")
        UUID tutorId,

        @Schema(description = "Status atual do processo", example = "ANALISE, PENDENTE_DOC, APROVADO, RECUSADO")
        @NotBlank(message = "O status do processo é obrigatório")
        String status
) {}