package com.artemanha.school.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter @Setter
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(optional = false)
    @JoinColumn(name = "turma_id")
    private Turma turma;

    @ManyToOne(optional = false)
    @JoinColumn(name = "situacao_id")
    private MatriculaSituacao situacao;

    private LocalDate dataMatricula;
    private LocalDate inicio;
    private LocalDate termino;

    @Column(length = 500)
    private String observacoes;
}
