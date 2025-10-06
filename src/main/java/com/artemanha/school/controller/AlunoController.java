package com.artemanha.school.controller;

import com.artemanha.school.entity.Aluno;
import com.artemanha.school.entity.MatriculaStatus;
import com.artemanha.school.repository.AlunoRepository;
import com.artemanha.school.repository.MatriculaStatusRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/alunos")
@CrossOrigin(origins = "*")
public class AlunoController {

    private final AlunoRepository alunoRepo;
    private final MatriculaStatusRepository statusRepo;

    public AlunoController(AlunoRepository alunoRepo, MatriculaStatusRepository statusRepo) {
        this.alunoRepo = alunoRepo;
        this.statusRepo = statusRepo;
    }

    // GET /api/alunos
    @GetMapping
    public List<Aluno> listar() {
        return alunoRepo.findAll();
    }

    // GET /api/alunos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Aluno> buscarPorId(@PathVariable Long id) {
        Optional<Aluno> aluno = alunoRepo.findById(id);
        return aluno.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /api/alunos
    @PostMapping
    public ResponseEntity<Aluno> salvar(@RequestBody Aluno aluno) {
        // ⚙️ Se o front enviar apenas o ID do status, precisamos buscar o objeto completo
        if (aluno.getStatus() != null && aluno.getStatus().getId() != null) {
            Optional<MatriculaStatus> statusOpt = statusRepo.findById(aluno.getStatus().getId());
            statusOpt.ifPresent(aluno::setStatus);
        }
        Aluno salvo = alunoRepo.save(aluno);
        return ResponseEntity.ok(salvo);
    }

    // PUT /api/alunos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
        return alunoRepo.findById(id).map(alunoExistente -> {
            alunoExistente.setNome(alunoAtualizado.getNome());
            alunoExistente.setTurma(alunoAtualizado.getTurma());
            alunoExistente.setResponsavel(alunoAtualizado.getResponsavel());
            alunoExistente.setMatricula(alunoAtualizado.getMatricula());

            if (alunoAtualizado.getStatus() != null && alunoAtualizado.getStatus().getId() != null) {
                Optional<MatriculaStatus> statusOpt = statusRepo.findById(alunoAtualizado.getStatus().getId());
                statusOpt.ifPresent(alunoExistente::setStatus);
            }

            Aluno salvo = alunoRepo.save(alunoExistente);
            return ResponseEntity.ok(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/alunos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!alunoRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alunoRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
