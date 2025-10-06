package com.artemanha.school.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🧒 Dados do aluno
    private String nome;
    private LocalDate dataNascimento;
    private String turma;
    private String matricula;

    // 🎓 Acadêmico
    private String periodo;
    private String ano;
    private String horario;
    private String professora;
    private String auxiliar;

    // 👨‍👩‍🏫 Responsável pedagógico
    private String responsavelPedagogico;
    private String parentesco;
    private String emailResponsavel;
    private String telefone1;
    private String telefone2;

    // 📊 Relacionamento com status (dropdown)
    @ManyToOne
    @JoinColumn(name = "status_id")
    private MatriculaStatus status;
}
