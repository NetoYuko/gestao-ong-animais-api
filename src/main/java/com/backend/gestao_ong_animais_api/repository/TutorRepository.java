package com.backend.gestao_ong_animais_api.repository;

import com.backend.gestao_ong_animais_api.model.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TutorRepository extends JpaRepository<Tutor, UUID> {
    Optional<Tutor> findByCpf(String cpf);
}
