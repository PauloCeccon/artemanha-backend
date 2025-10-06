package com.artemanha.school.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "turma")
@Getter
@Setter
public class Turma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dados principais
    private String nome;
    private String nomeResumido;
    private String curso;
    private String periodo;   // manhã/tarde/noite/semestre etc
    private String situacao;  // ativa, inativa, planejada etc
    private String turno;     // manhã/tarde/noite

    private Integer maximoAlunos;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate inicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate termino;
}
