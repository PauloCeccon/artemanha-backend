package com.artemanha.school.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "matricula_situacao")
public class MatriculaSituacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String descricao;

    public MatriculaSituacao() {}

    public MatriculaSituacao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
