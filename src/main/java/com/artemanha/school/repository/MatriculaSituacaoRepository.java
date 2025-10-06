package com.artemanha.school.repository;

import com.artemanha.school.entity.MatriculaSituacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaSituacaoRepository extends JpaRepository<MatriculaSituacao, Long> {
    Optional<MatriculaSituacao> findByDescricaoIgnoreCase(String descricao);
    boolean existsByDescricaoIgnoreCase(String descricao);
}
