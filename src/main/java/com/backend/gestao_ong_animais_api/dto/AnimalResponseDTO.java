package com.backend.gestao_ong_animais_api.dto;

import com.backend.gestao_ong_animais_api.model.Animal;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Objeto de resposta com os dados detalhados do animal")
public record AnimalResponseDTO(
        @Schema(description = "UUID único do animal", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,
        @Schema(description = "Nome do animal", example = "Caramelo")
        String nome,
        @Schema(description = "Espécie do animal", example = "CACHORRO")
        String especie,
        @Schema(description = "Idade aproximada em meses", example = "24")
        Integer idadeMeses,
        @Schema(description = "Status atual", example = "DISPONIVEL")
        String status,
        @Schema(description = "URL da foto armazenada no Supabase Storage", example = "https://supabase.com/.../caramelo.jpg")
        String fotoUrl,
        @Schema(description = "Data em que o animal foi cadastrado no sistema", example = "2026-10-01")
        LocalDate dataCadastro
) {
    public AnimalResponseDTO(Animal animal) {
        this(
                animal.getId(),
                animal.getNome(),
                animal.getEspecie(),
                animal.getIdadeMeses(),
                animal.getStatus(),
                animal.getFotoUrl(),
                animal.getDataCadastro()
        );
    }
}
