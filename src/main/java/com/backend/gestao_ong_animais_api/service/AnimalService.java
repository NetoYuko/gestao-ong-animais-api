package com.backend.gestao_ong_animais_api.service;

import com.backend.gestao_ong_animais_api.dto.AnimalRequestDTO;
import com.backend.gestao_ong_animais_api.dto.AnimalResponseDTO;
import com.backend.gestao_ong_animais_api.model.Animal;
import com.backend.gestao_ong_animais_api.repository.AnimalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository repository;

    public AnimalService(AnimalRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AnimalResponseDTO cadastrar(AnimalRequestDTO dto) {
        Animal animal = new Animal();
        animal.setNome(dto.nome());
        animal.setEspecie(dto.especie());
        animal.setIdadeMeses(dto.idadeMeses());
        animal.setStatus("DISPONIVEL");
        animal.setFotoUrl(dto.fotoUrl());

        Animal animalSalvo = repository.save(animal);
        return new AnimalResponseDTO(animalSalvo);
    }

    @Transactional(readOnly = true)
    public List<AnimalResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(AnimalResponseDTO::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public AnimalResponseDTO atualizar(UUID id, AnimalRequestDTO dto) {
        Animal animal = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));

        animal.setNome(dto.nome());
        animal.setEspecie(dto.especie());
        animal.setIdadeMeses(dto.idadeMeses());
        animal.setFotoUrl(dto.fotoUrl());

        Animal animalAtualizado = repository.save(animal);
        return new AnimalResponseDTO(animalAtualizado);
    }

    @Transactional
    public void excluir(UUID id) {
        Animal animal = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Animal não encontrado"));
        repository.delete(animal);
    }
}
