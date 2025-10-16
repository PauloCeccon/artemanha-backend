package com.artemanha.school.controller;

import com.artemanha.school.entity.Turma;
import com.artemanha.school.repository.TurmaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/turmas")
@CrossOrigin(origins = "*")
public class TurmaController {

    private final TurmaRepository turmaRepo;

    public TurmaController(TurmaRepository turmaRepo) {
        this.turmaRepo = turmaRepo;
    }

    // ✅ GET /api/turmas
    @GetMapping
    public List<Turma> listar() {
        return turmaRepo.findAll();
    }

    // ✅ GET /api/turmas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Turma> buscarPorId(@PathVariable Long id) {
        Optional<Turma> turma = turmaRepo.findById(id);
        return turma.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // ✅ POST /api/turmas
    @PostMapping
    public ResponseEntity<Turma> salvar(@RequestBody Turma turma) {
        Turma salva = turmaRepo.save(turma);
        return ResponseEntity.ok(salva);
    }

    // ✅ PUT /api/turmas/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Turma> atualizar(@PathVariable Long id, @RequestBody Turma atualizada) {
        return turmaRepo.findById(id).map(existente -> {
            existente.setNome(atualizada.getNome());
            existente.setNomeResumido(atualizada.getNomeResumido());
            existente.setCurso(atualizada.getCurso());
            existente.setPeriodo(atualizada.getPeriodo());
            existente.setSituacao(atualizada.getSituacao());
            existente.setTurno(atualizada.getTurno());
            existente.setMaximoAlunos(atualizada.getMaximoAlunos());
            existente.setInicio(atualizada.getInicio());
            existente.setTermino(atualizada.getTermino());

            // ✅ novos campos
            existente.setHorarioInicio(atualizada.getHorarioInicio());
            existente.setHorarioFim(atualizada.getHorarioFim());
            existente.setAno(atualizada.getAno());
            existente.setProfessora(atualizada.getProfessora());
            existente.setAuxiliar(atualizada.getAuxiliar());

            Turma salvo = turmaRepo.save(existente);
            return ResponseEntity.ok(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ DELETE /api/turmas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!turmaRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        turmaRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
