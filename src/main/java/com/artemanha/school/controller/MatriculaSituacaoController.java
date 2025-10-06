package com.artemanha.school.controller;

import com.artemanha.school.entity.MatriculaSituacao;
import com.artemanha.school.repository.MatriculaSituacaoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matricula-situacoes")
@CrossOrigin(origins = "*")
public class MatriculaSituacaoController {

    private final MatriculaSituacaoRepository repo;

    public MatriculaSituacaoController(MatriculaSituacaoRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<MatriculaSituacao> listar() {
        // retorna ordenado por descricao (opcional)
        return repo.findAll().stream()
                .sorted((a,b) -> a.getDescricao().compareToIgnoreCase(b.getDescricao()))
                .toList();
    }
}
