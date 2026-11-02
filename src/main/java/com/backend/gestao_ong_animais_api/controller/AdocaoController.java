package com.backend.gestao_ong_animais_api.controller;

import com.backend.gestao_ong_animais_api.dto.AdocaoRequestDTO;
import com.backend.gestao_ong_animais_api.dto.AdocaoResponseDTO;
import com.backend.gestao_ong_animais_api.service.AdocaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/adocoes")
@Tag(name = "Adoções", description = "Endpoints para gestão de processos de adoção")
public class AdocaoController {

    private final AdocaoService service;

    public AdocaoController(AdocaoService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar Adoção", description = "Inicia um novo processo de adoção vinculando um Tutor a um Animal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Adoção registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Falha: Animal ou Tutor informados não existem no banco de dados", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AdocaoResponseDTO> registrar(@RequestBody @Valid AdocaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(dto));
    }

    @Operation(summary = "Consultar Historico de Adoções do Animal", description = "Lista todo o histórico de tentativas de adoção de um animal específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<AdocaoResponseDTO>> listarPorAnimal(@PathVariable UUID animalId) {
        return ResponseEntity.ok(service.listarPorAnimal(animalId));
    }

    @Operation(summary = "Consultar Todas as Adoções", description = "Lista todo o histórico de tentativas de adoção de todos os animais.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Token inválido ou ausente)", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AdocaoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Atualizar Status do Processo de Adoção", description = "Atualiza o status de um processo de adoção existente. Se o status for alterado para APROVADO, o animal é automaticamente marcado como ADOTADO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Processo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Processo não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AdocaoResponseDTO> atualizarStatus(@PathVariable UUID id, @RequestBody @Valid AdocaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarStatus(id, dto));
    }
}
