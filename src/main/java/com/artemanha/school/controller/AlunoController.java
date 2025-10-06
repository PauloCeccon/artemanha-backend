package com.artemanha.school.controller;

import com.artemanha.school.entity.Aluno;
import com.artemanha.school.repository.AlunoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
public class AlunoController {

    private final AlunoRepository repo;

    public AlunoController(AlunoRepository repo) {
        this.repo = repo;
    }

    // GET /api/alunos
    @GetMapping
    public List<Aluno> listar() {
        return repo.findAll();
    }

    // GET /api/alunos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        Optional<Aluno> aluno = repo.findById(id);
        return aluno.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/alunos
    @PostMapping
    public Aluno salvar(@RequestBody Aluno aluno) {
        return repo.save(aluno);
    }

    // DELETE /api/alunos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
