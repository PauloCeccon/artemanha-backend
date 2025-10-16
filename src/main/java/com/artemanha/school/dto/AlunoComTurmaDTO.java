package com.artemanha.school.dto;

import com.artemanha.school.entity.MatriculaStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AlunoComTurmaDTO {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String turma;
    private String matricula;
    private String periodo;
    private String ano;
    private String horario;
    private String professora;
    private String auxiliar;
    private String responsavelPedagogico;
    private String parentesco;
    private String emailResponsavel;
    private String telefone1;
    private String telefone2;
    private MatriculaStatus status;
    private LocalDateTime dataCriacao;
}
