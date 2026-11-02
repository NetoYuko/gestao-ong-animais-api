package com.backend.gestao_ong_animais_api.controller;

import com.backend.gestao_ong_animais_api.dto.ProntuarioRequestDTO;
import com.backend.gestao_ong_animais_api.dto.ProntuarioResponseDTO;
import com.backend.gestao_ong_animais_api.service.ProntuarioMedicamentoService;
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
@RequestMapping("/api/prontuarios")
@Tag(name = "Prontuário Médico", description = "Endpoint para gestão de vacinas e medicamentos dos animais")
public class ProntuarioMedicamentoController {

    private final ProntuarioMedicamentoService service;

    public ProntuarioMedicamentoController(ProntuarioMedicamentoService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar Medicamento", description = "Adiciona uma nova aplicação ao histórico do animal.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Medicamento registrado com sucesso no histórico"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados ou formato de data inválido", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Falha: O Animal informado não existe", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ProntuarioResponseDTO> registrar(@RequestBody @Valid ProntuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(dto));
    }

    @Operation(summary = "Consultar Histórico", description = "Retorna todos os medicamentos aplicados em um animal específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico médico retornado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    @GetMapping("/animal/{animalId}")
    public ResponseEntity<List<ProntuarioResponseDTO>> listarPorAnimal(@PathVariable UUID animalId) {
        return ResponseEntity.ok(service.listarPorAnimal(animalId));
    }

    @Operation(summary = "Listar Todos os Prontuários", description = "Retorna a listagem completa de todos os medicamentos e vacinas aplicados em todos os animais da ONG.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Token ausente ou inválido)", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<ProntuarioResponseDTO>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }
}
