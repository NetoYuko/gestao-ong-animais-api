package com.backend.gestao_ong_animais_api.service;

import com.backend.gestao_ong_animais_api.dto.AdocaoRequestDTO;
import com.backend.gestao_ong_animais_api.dto.AdocaoResponseDTO;
import com.backend.gestao_ong_animais_api.model.Adocao;
import com.backend.gestao_ong_animais_api.model.Animal;
import com.backend.gestao_ong_animais_api.model.Tutor;
import com.backend.gestao_ong_animais_api.repository.AdocaoRepository;
import com.backend.gestao_ong_animais_api.repository.AnimalRepository;
import com.backend.gestao_ong_animais_api.repository.TutorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdocaoService {

    private final AdocaoRepository adocaoRepository;
    private final AnimalRepository animalRepository;
    private final TutorRepository tutorRepository;

    public AdocaoService(AdocaoRepository adocaoRepository, AnimalRepository animalRepository, TutorRepository tutorRepository) {
        this.adocaoRepository = adocaoRepository;
        this.animalRepository = animalRepository;
        this.tutorRepository = tutorRepository;
    }

    @Transactional
    public AdocaoResponseDTO registrar(AdocaoRequestDTO dto) {
        Animal animal = animalRepository.findById(dto.animalId())
                .orElseThrow(() -> new RuntimeException("Animal não encontrado."));
        Tutor tutor = tutorRepository.findById(dto.tutorId())
                .orElseThrow(() -> new RuntimeException("Tutor não encontrado."));

        if (!"DISPONIVEL".equalsIgnoreCase(animal.getStatus())) {
            throw new RuntimeException("Este animal não está disponível para um novo processo de adoção.");
        }

        Adocao adocao = new Adocao();
        adocao.setAnimal(animal);
        adocao.setTutor(tutor);
        adocao.setStatus(dto.status().toUpperCase());

        sincronizarStatusAnimal(adocao, animal);

        Adocao salvo = adocaoRepository.save(adocao);
        return new AdocaoResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public List<AdocaoResponseDTO> listarPorAnimal(UUID animalId) {
        return adocaoRepository.findByAnimalId(animalId).stream()
                .map(AdocaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AdocaoResponseDTO> listarTodos() {
        return adocaoRepository.findAll().stream()
                .map(AdocaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public AdocaoResponseDTO atualizarStatus(UUID id, AdocaoRequestDTO dto) {
        Adocao adocao = adocaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Processo de adoção não encontrado."));

        adocao.setStatus(dto.status().toUpperCase());

        sincronizarStatusAnimal(adocao, adocao.getAnimal());

        Adocao adocaoAtualizada = adocaoRepository.save(adocao);
        return new AdocaoResponseDTO(adocaoAtualizada);
    }

    private void sincronizarStatusAnimal(Adocao adocao, Animal animal) {
        String statusAdocao = adocao.getStatus();

        if ("APROVADO".equals(statusAdocao)) {
            animal.setStatus("ADOTADO");
            adocao.setDataConclusao(LocalDate.now());
        } else if ("RECUSADO".equals(statusAdocao)) {
            animal.setStatus("DISPONIVEL");
            adocao.setDataConclusao(LocalDate.now());
        } else {
            animal.setStatus("EM_ADOCAO");
            adocao.setDataConclusao(null);
        }

        animalRepository.save(animal);
    }
}
