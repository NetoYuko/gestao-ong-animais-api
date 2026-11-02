package com.backend.gestao_ong_animais_api.service;

import com.backend.gestao_ong_animais_api.dto.TutorRequestDTO;
import com.backend.gestao_ong_animais_api.dto.TutorResponseDTO;
import com.backend.gestao_ong_animais_api.model.Tutor;
import com.backend.gestao_ong_animais_api.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TutorService {

    private final TutorRepository repository;

    public TutorService(TutorRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TutorResponseDTO cadastrar(TutorRequestDTO dto) {
        if (repository.findByCpf(dto.cpf()).isPresent()) {
            throw new RuntimeException("Já existe um tutor cadastrado com este CPF.");
        }

        Tutor tutor = new Tutor();
        tutor.setNome(dto.nome());
        tutor.setCpf(dto.cpf());
        tutor.setTelefone(dto.telefone());
        tutor.setEndereco(dto.endereco());

        Tutor salvo = repository.save(tutor);
        return new TutorResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<TutorResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(TutorResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public TutorResponseDTO atualizar(UUID id, TutorRequestDTO dto) {
        Tutor tutor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado."));

        if (!tutor.getCpf().equals(dto.cpf()) && repository.findByCpf(dto.cpf()).isPresent()) {
            throw new RuntimeException("O novo CPF informado já pertence a outro tutor.");
        }

        tutor.setNome(dto.nome());
        tutor.setCpf(dto.cpf());
        tutor.setTelefone(dto.telefone());
        tutor.setEndereco(dto.endereco());

        return new TutorResponseDTO(repository.save(tutor));
    }

    @Transactional
    public void excluir(UUID id) {
        Tutor tutor = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado."));
        repository.delete(tutor);
    }
}
