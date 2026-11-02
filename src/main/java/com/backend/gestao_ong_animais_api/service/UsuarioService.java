package com.backend.gestao_ong_animais_api.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import com.backend.gestao_ong_animais_api.dto.UsuarioAtualizarRequestDTO;
import com.backend.gestao_ong_animais_api.dto.UsuarioRequestDTO;
import com.backend.gestao_ong_animais_api.dto.UsuarioResponseDTO;
import com.backend.gestao_ong_animais_api.dto.UsuarioSenhaRequestDTO;
import com.backend.gestao_ong_animais_api.model.Usuario;
import com.backend.gestao_ong_animais_api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {
        if (repository.findByEmail(dto.email()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado no sistema.");
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.nome());
        usuario.setEmail(dto.email());
        usuario.setPerfil(dto.perfil());
        usuario.setSenhaHash(passwordEncoder.encode(dto.senha()));

        Usuario salvo = repository.save(usuario);
        return new UsuarioResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UsuarioResponseDTO atualizarDadosBasicos(UUID id, UsuarioAtualizarRequestDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.email() != null && !dto.email().isBlank() && !dto.email().equals(usuario.getEmail())) {
            if (repository.findByEmail(dto.email()).isPresent()) {
                throw new RuntimeException("Este e-mail já está sendo utilizado por outro usuário.");
            }
            usuario.setEmail(dto.email());
        }

        if (dto.nome() != null && !dto.nome().isBlank()) {
            usuario.setNome(dto.nome());
        }

        if (dto.perfil() != null && !dto.perfil().isBlank()) {
            usuario.setPerfil(dto.perfil());
        }

        return new UsuarioResponseDTO(repository.save(usuario));
    }

    @Transactional
    public void alterarSenha(UUID id, UsuarioSenhaRequestDTO dto) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(dto.senhaAtual(), usuario.getSenhaHash())) {
            throw new RuntimeException("A senha atual informada está incorreta.");
        }

        usuario.setSenhaHash(passwordEncoder.encode(dto.novaSenha()));
        repository.save(usuario);
    }

    @Transactional
    public void excluir(UUID id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        repository.delete(usuario);
    }
}
