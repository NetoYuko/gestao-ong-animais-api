package com.backend.gestao_ong_animais_api.controller;

import com.backend.gestao_ong_animais_api.dto.UsuarioAtualizarRequestDTO;
import com.backend.gestao_ong_animais_api.dto.UsuarioRequestDTO;
import com.backend.gestao_ong_animais_api.dto.UsuarioResponseDTO;
import com.backend.gestao_ong_animais_api.dto.UsuarioSenhaRequestDTO;
import com.backend.gestao_ong_animais_api.service.UsuarioService;
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
@RequestMapping("/api/usuarios")
@Tag(name = "Usuários", description = "Endpoint para gestão de usuários(Visualizador/Admin) da ONG")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar Usuário", description = "Registra um novo usuário no sistema. Impede o cadastro de e-mails duplicados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou e-mail já existente", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(@RequestBody @Valid UsuarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(dto));
    }

    @Operation(summary = "Listar Usuários", description = "Retorna a listagem de todos os usuários cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listar() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @Operation(summary = "Atualizar Dados básicos do Usuário", description = "Atualiza nome, e-mail ou perfil de um usuário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou e-mail já em uso", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> atualizarDadosBasicos(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioAtualizarRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarDadosBasicos(id, dto));
    }

    @Operation(summary = "Alterar Senha do Usuário", description = "Endpoint exclusivo para atualização segura de credenciais. Exige a senha atual para autorizar a mudança.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso (sem retorno de corpo JSON)"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (ex: senha atual incorreta)", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @PatchMapping("/{id}/senha")
    public ResponseEntity<Void> alterarSenha(
            @PathVariable UUID id,
            @RequestBody @Valid UsuarioSenhaRequestDTO dto) {
        service.alterarSenha(id, dto);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir Usuário", description = "Remove permanentemente um usuário do sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable UUID id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
