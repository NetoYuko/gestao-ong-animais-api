package com.backend.gestao_ong_animais_api.dto;

import com.backend.gestao_ong_animais_api.model.ProntuarioMedicamento;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Objeto de resposta com os detalhes de uma aplicação médica.")
public record ProntuarioResponseDTO(
        @Schema(description = "ID único do registro médico", example = "990e8400-e29b-41d4-a716-446655440000")
        UUID id,
        @Schema(description = "ID do animal que recebeu a medicação", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID animalId,
        @Schema(description = "Nome do medicamento aplicado", example = "Bravecto")
        String nomeMedicamento,
        @Schema(description = "Categoria do medicamento", example = "ANTIPULGAS")
        String tipo,
        @Schema(description = "Data da aplicação", example = "2026-10-02")
        LocalDate dataAplicacao,
        @Schema(description = "Data agendada para a próxima dose", example = "2027-01-02")
        LocalDate dataProximaDose,
        @Schema(description = "Observações clínicas", example = "Animal apresentou leve sonolência, mas sem reações alérgicas graves.")
        String observacoes
) {
    public ProntuarioResponseDTO(ProntuarioMedicamento p) {
        this(
                p.getId(),
                p.getAnimal().getId(),
                p.getNomeMedicamento(),
                p.getTipo(),
                p.getDataAplicacao(),
                p.getDataProximaDose(),
                p.getObservacoes()
        );
    }
}
