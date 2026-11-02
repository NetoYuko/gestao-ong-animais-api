package com.backend.gestao_ong_animais_api.controller;

import com.backend.gestao_ong_animais_api.dto.TutorRequestDTO;
import com.backend.gestao_ong_animais_api.dto.TutorResponseDTO;
import com.backend.gestao_ong_animais_api.service.TutorService;
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
@RequestMapping("/api/tutores")
@Tag(name = "Tutores", description = "Endpoint para gestão de interessados em adoção")
public class TutorController {

    private final TutorService service;

    public TutorController(TutorService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar Tutor", description = "Registra uma nova pessoa interessada em adotar. (O sistema impede o cadastro de CPFs duplicados.)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tutor cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (dados incorretos ou CPF já existente)", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Usuário sem permissão)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<TutorResponseDTO> cadastrar(@RequestBody @Valid TutorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @Operation(summary = "Listar Tutores", description = "Retorna o cadastro de todos os tutores cadastrados na ONG.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<TutorResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Atualizar Tutor", description = "Altera os dados de contato ou endereço de um tutor existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tutor atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (dados enviados ou tentativa de usar CPF de outro tutor)", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<TutorResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid TutorRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Excluir Tutor", description = "Remove um tutor do sistema (a exclusão falhará se houver adoções vinculadas a ele).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tutor excluído com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Tutor não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
