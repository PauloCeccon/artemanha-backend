// src/main/java/com/artemanha/school/repository/MatriculaRepository.java
package com.artemanha.school.repository;

import com.artemanha.school.entity.Matricula;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface MatriculaRepository extends CrudRepository<Matricula, Long> {

    boolean existsByAluno_IdAndTurma_IdAndSituacao_Id(Long alunoId, Long turmaId, Long situacaoId);

    long countByTurma_IdAndSituacao_Id(Long turmaId, Long situacaoId);

    // Para achar outra matrícula ATIVA do mesmo aluno, se existir
    Optional<Matricula> findFirstByAluno_IdAndSituacao_DescricaoIgnoreCaseOrderByDataMatriculaDesc(
            Long alunoId, String descricaoSituacao);
}
