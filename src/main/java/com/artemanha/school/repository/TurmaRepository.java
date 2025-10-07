package com.artemanha.school.repository;

import com.artemanha.school.entity.Turma;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TurmaRepository extends JpaRepository<Turma, Long> {

    long countBySituacaoIgnoreCase(String situacao);
}
