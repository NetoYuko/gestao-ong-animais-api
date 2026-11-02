package com.backend.gestao_ong_animais_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para cadastro e atualização de um animal")
public record AnimalRequestDTO(
        @Schema(description = "Nome do animal", example = "Caramelo")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(description = "Espécie do animal (ex: CACHORRO, GATO", example = "CACHORRO")
        @NotBlank(message = "A espécie é obrigatória")
        String especie,

        @Schema(description = "Idade aproximada em meses", example = "24")
        Integer idadeMeses,

        @Schema(description = "Status atual de adoção", example = "DISPONIVEL")
        @NotBlank(message = "O status é obrigatório")
        String status,

        @Schema(description = "URL da foto salva no Supabase Storage", example = "https://...")
        String fotoUrl
) {}
