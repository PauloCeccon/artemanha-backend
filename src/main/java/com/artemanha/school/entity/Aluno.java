package com.artemanha.school.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Getter
@Setter
public class Aluno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🧒 Dados do aluno
    private String nome;

    @JsonFormat(pattern = "yyyy-MM-dd", shape = JsonFormat.Shape.STRING)
    private LocalDate dataNascimento;

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

    // 🕒 Data e hora em que o aluno foi criado
    @Column(name = "data_criacao", updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void aoCriar() {
        this.dataCriacao = LocalDateTime.now();
    }
}
