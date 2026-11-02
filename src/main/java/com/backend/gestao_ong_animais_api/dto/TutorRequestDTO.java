package com.backend.gestao_ong_animais_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Objeto de requisição para cadastro e atualização de um tutor")
public record TutorRequestDTO(
        @Schema(description = "Nome completo do tutor", example = "João da Silva")
        @NotBlank(message = "O nome é obrigatório")
        String nome,

        @Schema(description = "CPF do tutor", example = "123.456.789-00")
        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        @Schema(description = "Telefone de contato", example = "(82) 99999-9999")
        @NotBlank(message = "O telefone é obrigatório")
        String telefone,

        @Schema(description = "Endereço completo", example = "Rua das Flores, 123")
        @NotBlank(message = "O endereço é obrigatório")
        String endereco
) {}
