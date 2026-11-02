package com.backend.gestao_ong_animais_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Objeto de requisição para registrar uma nova aplicação médica no histórico do animal")
public record ProntuarioRequestDTO(
        @Schema(description = "ID do animal que receberá o medicamento", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotNull(message = "O ID do animal é obrigatório")
        UUID animalId,

        @Schema(description = "Nome do medicamento", example = "Bravecto")
        @NotBlank(message = "O nome do medicamento é obrigatório")
        String nomeMedicamento,

        @Schema(description = "Tipo do medicamento", example = "VACINA, VERMIFUGO, ANTIPULGAS, ANTIBIOTICO")
        @NotBlank(message = "O tipo de medicamento é obrigatório")
        String tipo,

        @Schema(description = "Data em que o medicamento foi aplicado", example = "2026-10-02")
        @NotNull(message = "A data de aplicação é obrigatória")
        LocalDate dataAplicacao,

        @Schema(description = "Data agendada da próxima dose (opcional)",example = "2027-01-02")
        LocalDate dataProximaDose,

        @Schema(description = "Observações clínicas gerais sobre a aplicação ou reações", example = "Animal apresentou leve sonolência, mas sem reações alérgicas graves.")
        String observacoes
) {}
