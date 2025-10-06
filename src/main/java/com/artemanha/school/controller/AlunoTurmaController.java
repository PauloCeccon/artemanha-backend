package com.artemanha.school.controller;

import com.artemanha.school.dto.AlunoTurmaRequest;
import com.artemanha.school.entity.Aluno;
import com.artemanha.school.entity.AlunoTurma;
import com.artemanha.school.entity.Turma;
import com.artemanha.school.repository.AlunoRepository;
import com.artemanha.school.repository.AlunoTurmaRepository;
import com.artemanha.school.repository.TurmaRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aluno-turmas")
@CrossOrigin(origins = "*")
public class AlunoTurmaController {

    private final AlunoTurmaRepository atRepo;
    private final AlunoRepository alunoRepo;
    private final TurmaRepository turmaRepo;

    public AlunoTurmaController(AlunoTurmaRepository atRepo, AlunoRepository alunoRepo, TurmaRepository turmaRepo) {
        this.atRepo = atRepo;
        this.alunoRepo = alunoRepo;
        this.turmaRepo = turmaRepo;
    }

    // GET /api/aluno-turmas
    @GetMapping
    public List<AlunoTurma> listar(
            @RequestParam(required = false) Long alunoId,
            @RequestParam(required = false) Long turmaId
    ) {
        if (alunoId != null) return atRepo.findByAluno_Id(alunoId);
        if (turmaId != null) return atRepo.findByTurma_Id(turmaId);
        return atRepo.findAll();
    }

    // POST /api/aluno-turmas
    @PostMapping
    public ResponseEntity<?> criar(@RequestBody AlunoTurmaRequest req) {
        if (req.getAlunoId() == null || req.getTurmaId() == null) {
            return ResponseEntity.badRequest().body("alunoId e turmaId são obrigatórios.");
        }

        if (atRepo.existsByAluno_IdAndTurma_Id(req.getAlunoId(), req.getTurmaId())) {
            return ResponseEntity.badRequest().body("Aluno já está vinculado a esta turma.");
        }

        Optional<Aluno> alunoOpt = alunoRepo.findById(req.getAlunoId());
        Optional<Turma> turmaOpt = turmaRepo.findById(req.getTurmaId());
        if (alunoOpt.isEmpty() || turmaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Aluno ou Turma não encontrados.");
        }

        AlunoTurma at = new AlunoTurma();
        at.setAluno(alunoOpt.get());
        at.setTurma(turmaOpt.get());

        // dataMatricula opcional
        if (req.getDataMatricula() != null && !req.getDataMatricula().isBlank()) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            at.setDataMatricula(LocalDate.parse(req.getDataMatricula(), fmt));
        }

        if (req.getAtivo() != null) {
            at.setAtivo(req.getAtivo());
        }

        AlunoTurma salvo = atRepo.save(at);
        return ResponseEntity.ok(salvo);
    }

    // DELETE /api/aluno-turmas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!atRepo.existsById(id)) return ResponseEntity.notFound().build();
        atRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
