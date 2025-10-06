package com.artemanha.school.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlunoTurmaRequest {
    private Long alunoId;
    private Long turmaId;
    private String dataMatricula;
    private Boolean ativo;
}
