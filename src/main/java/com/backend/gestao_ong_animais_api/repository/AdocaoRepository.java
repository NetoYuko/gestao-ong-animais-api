package com.backend.gestao_ong_animais_api.repository;

import com.backend.gestao_ong_animais_api.model.Adocao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdocaoRepository extends JpaRepository<Adocao, UUID> {
    List<Adocao> findByAnimalId(UUID animalId);
}
