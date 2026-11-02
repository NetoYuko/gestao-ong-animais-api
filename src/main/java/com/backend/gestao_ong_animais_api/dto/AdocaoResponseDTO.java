package com.backend.gestao_ong_animais_api.dto;

import com.backend.gestao_ong_animais_api.model.Adocao;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Objeto de resposta com os detalhes do processo de adoção. Retorna apenas os IDs do animal e tutor para evitar recursão infinita.")
public record AdocaoResponseDTO(
        @Schema(description = "ID único do processo de adoção", example = "770e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @Schema(description = "ID do animal associado", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID animalId,
        @Schema(description = "ID do tutor associado", example = "550e8400-e29b-41d4-a716-446655440000")
        UUID tutorId,
        @Schema(description = "Status atual do processo", example = "ANALISE/PENDENTE_DOC/APROVADO/RECUSADO")
        String status,
        @Schema(description = "Data de início do processo", example = "2026-10-01")
        LocalDate dataInicio,
        @Schema(description = "Data de conclusão do processo (preenchida automaticamente ao aprovar)", example = "2026-10-15")
        LocalDate dataConclusao
) {
    public AdocaoResponseDTO(Adocao adocao) {
        this(
                adocao.getId(),
                adocao.getAnimal().getId(),
                adocao.getTutor().getId(),
                adocao.getStatus(),
                adocao.getDataInicio(),
                adocao.getDataConclusao()
        );
    }
}
