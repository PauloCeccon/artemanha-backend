package com.artemanha.school.dto;

import com.artemanha.school.entity.MatriculaStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class AlunoComTurmaDTO {
    private Long id;
    private String nome;
    private LocalDate dataNascimento;

    private String turma;
    private String matricula;

    // Campos vindos da turma
    private String professora;
    private String auxiliar;
    private String horarioInicio;
    private String horarioFim;
    private String periodo;
    private String ano;

    private String responsavelPedagogico;
    private String parentesco;
    private String emailResponsavel;
    private String telefone1;
    private String telefone2;
    private MatriculaStatus status;
    private LocalDateTime dataCriacao;
}
