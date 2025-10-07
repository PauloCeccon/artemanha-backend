package com.artemanha.school.repository;

import com.artemanha.school.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    // Conta alunos ativos (status_id = 1)
    long countByStatus_Id(Long statusId);

    // Conta alunos criados nos últimos 365 dias
    long countByDataCriacaoAfter(LocalDateTime data);
}
