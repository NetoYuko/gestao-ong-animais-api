package com.backend.gestao_ong_animais_api.controller;

import com.backend.gestao_ong_animais_api.dto.AnimalRequestDTO;
import com.backend.gestao_ong_animais_api.dto.AnimalResponseDTO;
import com.backend.gestao_ong_animais_api.service.AnimalService;
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
@RequestMapping("/api/animais")
@Tag(name = "Animais", description = "Endpoint para gestão do catálogo de animais da ONG")
public class AnimalController {

    private final AnimalService service;

    public AnimalController(AnimalService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar Animal", description = "Cadastra um novo animal no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Animal cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Usuário sem permissão)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<AnimalResponseDTO> cadastrar(@RequestBody @Valid AnimalRequestDTO dto) {
        AnimalResponseDTO response = service.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Listar Animais", description = "Retorna a listagem de todos os animais. Acessível a Visualizadores e Admins.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Token inválido)", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<AnimalResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Atualizar Animal", description = "Atualiza os dados cadastrais de um animal existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Animal atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Usuário sem permissão)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> atualizar(@PathVariable UUID id, @RequestBody @Valid AnimalRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @Operation(summary = "Excluir Animal", description = "Remove permanentemente um animal do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Animal excluído com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Usuário sem permissão)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Animal não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
