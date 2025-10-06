package com.artemanha.school.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "aluno_turma",
        uniqueConstraints = @UniqueConstraint(columnNames = {"aluno_id", "turma_id"})
)
@Getter
@Setter
public class AlunoTurma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // lado "muitos-para-um" com Aluno
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    // lado "muitos-para-um" com Turma
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "turma_id")
    private Turma turma;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataMatricula = LocalDate.now();

    private Boolean ativo = true;
}
