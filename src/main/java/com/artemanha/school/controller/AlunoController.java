package com.artemanha.school.controller;

import com.artemanha.school.dto.AlunoComTurmaDTO;
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

    // ✅ LISTA todos os alunos com nome da turma e ID da matrícula
    @GetMapping
    public List<AlunoComTurmaDTO> listar() {
        return alunoRepo.findAllWithTurma();
    }

    // ✅ Busca aluno por ID
    @GetMapping("/{id}")
    public ResponseEntity<AlunoComTurmaDTO> buscarPorId(@PathVariable Long id) {
        return alunoRepo.findAllWithTurma().stream()
                .filter(a -> a.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Cria novo aluno
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody Aluno aluno) {
        if (aluno.getNome() == null || aluno.getNome().isBlank()) {
            return ResponseEntity.badRequest().body("Nome do aluno é obrigatório.");
        }

        if (aluno.getStatus() != null && aluno.getStatus().getId() != null) {
            Optional<MatriculaStatus> statusOpt = statusRepo.findById(aluno.getStatus().getId());
            statusOpt.ifPresent(aluno::setStatus);
        }

        Aluno salvo = alunoRepo.save(aluno);
        return ResponseEntity.ok(salvo);
    }

    // ✅ Atualiza dados básicos do aluno
    @PutMapping("/{id}")
    public ResponseEntity<Aluno> atualizar(@PathVariable Long id, @RequestBody Aluno alunoAtualizado) {
        return alunoRepo.findById(id).map(alunoExistente -> {
            alunoExistente.setNome(alunoAtualizado.getNome());
            alunoExistente.setDataNascimento(alunoAtualizado.getDataNascimento());
            alunoExistente.setResponsavelPedagogico(alunoAtualizado.getResponsavelPedagogico());
            alunoExistente.setParentesco(alunoAtualizado.getParentesco());
            alunoExistente.setEmailResponsavel(alunoAtualizado.getEmailResponsavel());
            alunoExistente.setTelefone1(alunoAtualizado.getTelefone1());
            alunoExistente.setTelefone2(alunoAtualizado.getTelefone2());

            if (alunoAtualizado.getStatus() != null && alunoAtualizado.getStatus().getId() != null) {
                Optional<MatriculaStatus> statusOpt = statusRepo.findById(alunoAtualizado.getStatus().getId());
                statusOpt.ifPresent(alunoExistente::setStatus);
            }

            Aluno salvo = alunoRepo.save(alunoExistente);
            return ResponseEntity.ok(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ Exclui aluno
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!alunoRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        alunoRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
