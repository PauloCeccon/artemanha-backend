package com.artemanha.school.repository;

import com.artemanha.school.entity.AlunoTurma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlunoTurmaRepository extends JpaRepository<AlunoTurma, Long> {
    List<AlunoTurma> findByAluno_Id(Long alunoId);
    List<AlunoTurma> findByTurma_Id(Long turmaId);
    boolean existsByAluno_IdAndTurma_Id(Long alunoId, Long turmaId);
}
