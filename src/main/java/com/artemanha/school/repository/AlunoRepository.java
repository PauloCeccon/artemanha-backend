package com.artemanha.school.repository;

import com.artemanha.school.dto.AlunoComTurmaDTO;
import com.artemanha.school.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // Conta alunos ativos (status_id = 1)
    long countByStatus_Id(Long statusId);

    // Conta alunos criados nos últimos 365 dias
    long countByDataCriacaoAfter(LocalDateTime data);

    // ✅ Retorna alunos com nome da turma e ID da matrícula
    @Query("SELECT new com.artemanha.school.dto.AlunoComTurmaDTO(" +
            "a.id, " +
            "a.nome, " +
            "a.dataNascimento, " +
            "t.nome, " +
            "CAST(m.id AS string), " +
            "a.periodo, " +
            "a.ano, " +
            "a.horario, " +
            "a.professora, " +
            "a.auxiliar, " +
            "a.responsavelPedagogico, " +
            "a.parentesco, " +
            "a.emailResponsavel, " +
            "a.telefone1, " +
            "a.telefone2, " +
            "a.status, " +
            "a.dataCriacao) " +
            "FROM Aluno a " +
            "LEFT JOIN Matricula m ON m.aluno.id = a.id " +
            "LEFT JOIN Turma t ON t.id = m.turma.id")
    List<AlunoComTurmaDTO> findAllWithTurma();
}
