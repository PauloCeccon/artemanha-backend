package com.artemanha.school.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String turma;
    private String responsavel;
    private String matricula;

    // Relacionamento com a tabela matricula_status
    @ManyToOne
    @JoinColumn(name = "status_id")
    private MatriculaStatus status;
}
