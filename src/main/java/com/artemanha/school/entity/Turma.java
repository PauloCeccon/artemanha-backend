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

    @Column(name = "nome_resumido")
    private String nomeResumido;

    private String curso;
    private String situacao;
    private String turno;

    @Column(name = "maximo_alunos")
    private Integer maximoAlunos;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate inicio;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate termino;

    // Campos adicionais (mapeados corretamente para snake_case no banco)
    @Column(name = "professora")
    private String professora;

    @Column(name = "auxiliar")
    private String auxiliar;

    @Column(name = "horario_inicio")
    private String horarioInicio;

    @Column(name = "horario_fim")
    private String horarioFim;

    @Column(name = "periodo")
    private String periodo;

    @Column(name = "ano")
    private String ano;
}
