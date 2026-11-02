package com.backend.gestao_ong_animais_api.controller;

import com.backend.gestao_ong_animais_api.dto.AuthRequestDTO;
import com.backend.gestao_ong_animais_api.dto.AuthResponseDTO;
import com.backend.gestao_ong_animais_api.model.Usuario;
import com.backend.gestao_ong_animais_api.security.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autenticação", description = "Endpoints para login e geração de token JWT")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @Operation(summary = "Realizar Login", description = "Valida o e-mail e senha do usuário. Em caso de sucesso, devolve um token JWT com 2 horas de validade para ser utilizado nas próximas requisições.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação (e-mail ou senha em branco/inválidos)", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado (Credenciais incorretas)", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid AuthRequestDTO dto) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.senha());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((Usuario) auth.getPrincipal());

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
