package com.artemanha.school.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String turma;
    private String email;
}
