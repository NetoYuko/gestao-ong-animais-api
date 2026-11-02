package com.backend.gestao_ong_animais_api.service;

import com.backend.gestao_ong_animais_api.dto.ProntuarioRequestDTO;
import com.backend.gestao_ong_animais_api.dto.ProntuarioResponseDTO;
import com.backend.gestao_ong_animais_api.model.Animal;
import com.backend.gestao_ong_animais_api.model.ProntuarioMedicamento;
import com.backend.gestao_ong_animais_api.repository.AnimalRepository;
import com.backend.gestao_ong_animais_api.repository.ProntuarioMedicamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProntuarioMedicamentoService {

    private final ProntuarioMedicamentoRepository prontuarioRepository;
    private final AnimalRepository animalRepository;

    public ProntuarioMedicamentoService(ProntuarioMedicamentoRepository prontuarioRepository, AnimalRepository animalRepository) {
        this.prontuarioRepository = prontuarioRepository;
        this.animalRepository = animalRepository;
    }

    @Transactional
    public ProntuarioResponseDTO registrar(ProntuarioRequestDTO dto) {
        Animal animal = animalRepository.findById(dto.animalId())
                .orElseThrow(() -> new RuntimeException("Operação falhou: Animal não encontrado."));

        ProntuarioMedicamento prontuario = new ProntuarioMedicamento();
        prontuario.setAnimal(animal);
        prontuario.setNomeMedicamento(dto.nomeMedicamento());
        prontuario.setTipo(dto.tipo());
        prontuario.setDataAplicacao(dto.dataAplicacao());
        prontuario.setDataProximaDose(dto.dataProximaDose());
        prontuario.setObservacoes(dto.observacoes());

        ProntuarioMedicamento salvo = prontuarioRepository.save(prontuario);
        return new ProntuarioResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<ProntuarioResponseDTO> listarPorAnimal(UUID animalId) {
        return prontuarioRepository.findByAnimalId(animalId).stream()
                .map(ProntuarioResponseDTO::new)
                .collect(Collectors.toList());
    }

    public List<ProntuarioResponseDTO> listarTodos() {
        return prontuarioRepository.findAll().stream()
                .map(ProntuarioResponseDTO::new)
                .collect(Collectors.toList());
    }
}
