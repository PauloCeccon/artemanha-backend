package com.artemanha.school.config;

import com.artemanha.school.entity.MatriculaSituacao;
import com.artemanha.school.repository.MatriculaSituacaoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MatriculaSituacaoSeeder implements CommandLineRunner {

    private final MatriculaSituacaoRepository repo;

    public MatriculaSituacaoSeeder(MatriculaSituacaoRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        List<String> valores = List.of(
                "Ativa", "Trancada", "Cancelada", "Transferida", "Concluída"
        );

        for (String v : valores) {
            if (!repo.existsByDescricaoIgnoreCase(v)) {
                repo.save(new MatriculaSituacao(v));
            }
        }
    }
}
