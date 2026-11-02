package com.backend.gestao_ong_animais_api.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "prontuarios_medicamentos")
public class ProntuarioMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;

    @Column(name = "nome_medicamento", nullable = false)
    private String nomeMedicamento;

    @Column(nullable = false)
    private String tipo;

    @Column(name = "data_aplicacao", nullable = false)
    private LocalDate dataAplicacao;

    @Column(name = "data_proxima_dose")
    private LocalDate dataProximaDose;

    @Column(columnDefinition = "TEXT")
    private String observacoes;
}
