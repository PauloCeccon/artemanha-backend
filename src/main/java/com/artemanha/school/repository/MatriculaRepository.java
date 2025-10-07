package com.artemanha.school.repository;

import com.artemanha.school.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {

    // ✅ Conta todas as matrículas com a situação específica (ex: 1 = Ativa)
    long countBySituacao_Id(Long situacaoId);

    boolean existsByAluno_IdAndTurma_IdAndSituacao_Id(Long alunoId, Long turmaId, Long situacaoId);

    long countByTurma_IdAndSituacao_Id(Long turmaId, Long situacaoId);

    Optional<Matricula> findFirstByAluno_IdAndSituacao_DescricaoIgnoreCaseOrderByDataMatriculaDesc(
            Long alunoId, String descricaoSituacao);
}
