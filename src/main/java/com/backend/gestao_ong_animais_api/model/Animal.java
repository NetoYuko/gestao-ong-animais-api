package com.backend.gestao_ong_animais_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@Table(name = "animais")
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String especie;

    @Column(name = "idade_meses")
    private Integer idadeMeses;

    @Column(nullable = false)
    private String status;

    @Column(name = "foto_url")
    private String fotoUrl;

    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    public Animal() {}

    @PrePersist
    protected void onCreate() {
        this.dataCadastro = LocalDate.now();
    }

}
