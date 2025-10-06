// src/main/java/com/artemanha/school/controller/MatriculaController.java
package com.artemanha.school.controller;

import com.artemanha.school.entity.Aluno;
import com.artemanha.school.entity.Matricula;
import com.artemanha.school.entity.MatriculaSituacao;
import com.artemanha.school.entity.Turma;
import com.artemanha.school.repository.AlunoRepository;
import com.artemanha.school.repository.MatriculaRepository;
import com.artemanha.school.repository.MatriculaSituacaoRepository;
import com.artemanha.school.repository.TurmaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/matriculas")
@CrossOrigin(origins = "*")
public class MatriculaController {

    private final MatriculaRepository matriculaRepo;
    private final MatriculaSituacaoRepository situacaoRepo;
    private final AlunoRepository alunoRepo;
    private final TurmaRepository turmaRepo;

    public MatriculaController(MatriculaRepository matriculaRepo,
                               MatriculaSituacaoRepository situacaoRepo,
                               AlunoRepository alunoRepo,
                               TurmaRepository turmaRepo) {
        this.matriculaRepo = matriculaRepo;
        this.situacaoRepo = situacaoRepo;
        this.alunoRepo = alunoRepo;
        this.turmaRepo = turmaRepo;
    }

    @GetMapping
    public Iterable<Matricula> listar() {
        return matriculaRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Matricula> buscar(@PathVariable Long id) {
        return matriculaRepo.findById(id).map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Matricula req) {
        if (req.getAluno() == null || req.getAluno().getId() == null ||
                req.getTurma() == null || req.getTurma().getId() == null) {
            return ResponseEntity.badRequest().body("Aluno e Turma são obrigatórios.");
        }

        Optional<Aluno> alunoOpt = alunoRepo.findById(req.getAluno().getId());
        Optional<Turma> turmaOpt = turmaRepo.findById(req.getTurma().getId());
        if (alunoOpt.isEmpty() || turmaOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Aluno ou Turma inválidos.");
        }

        // Situação: default "Ativa" se não enviada
        MatriculaSituacao situacaoAtiva = situacaoRepo
                .findByDescricaoIgnoreCase("Ativa")
                .orElseThrow();

        MatriculaSituacao situacao = null;
        if (req.getSituacao() != null && req.getSituacao().getId() != null) {
            situacao = situacaoRepo.findById(req.getSituacao().getId()).orElse(null);
        }
        if (situacao == null) situacao = situacaoAtiva;

        // Checa duplicidade de matrícula ATIVA do mesmo aluno na mesma turma
        if (situacao.getId().equals(situacaoAtiva.getId()) &&
                matriculaRepo.existsByAluno_IdAndTurma_IdAndSituacao_Id(
                        alunoOpt.get().getId(), turmaOpt.get().getId(), situacaoAtiva.getId())) {
            return ResponseEntity.status(409).body("Aluno já possui matrícula ATIVA nesta turma.");
        }

        // Capacidade
        Integer max = turmaOpt.get().getMaximoAlunos();
        if (max != null && max > 0 && situacao.getId().equals(situacaoAtiva.getId())) {
            long ocupadas = matriculaRepo.countByTurma_IdAndSituacao_Id(turmaOpt.get().getId(), situacaoAtiva.getId());
            if (ocupadas >= max) {
                return ResponseEntity.unprocessableEntity().body("Turma sem vagas.");
            }
        }

        Matricula m = new Matricula();
        m.setAluno(alunoOpt.get());
        m.setTurma(turmaOpt.get());
        m.setDataMatricula(req.getDataMatricula());
        m.setInicio(req.getInicio());
        m.setTermino(req.getTermino());
        m.setSituacao(situacao);
        m.setObservacoes(req.getObservacoes());

        Matricula salvo = matriculaRepo.save(m);

        // 🔄 Sincroniza perfil do aluno com esta matrícula
        aplicarMatriculaNoPerfilAluno(salvo);

        return ResponseEntity.created(URI.create("/api/matriculas/" + salvo.getId())).body(salvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody Matricula req) {
        return matriculaRepo.findById(id).map(m -> {

            if (req.getAluno() != null && req.getAluno().getId() != null) {
                alunoRepo.findById(req.getAluno().getId()).ifPresent(m::setAluno);
            }
            if (req.getTurma() != null && req.getTurma().getId() != null) {
                turmaRepo.findById(req.getTurma().getId()).ifPresent(m::setTurma);
            }
            if (req.getDataMatricula() != null) m.setDataMatricula(req.getDataMatricula());
            if (req.getInicio() != null) m.setInicio(req.getInicio());
            if (req.getTermino() != null) m.setTermino(req.getTermino());

            if (req.getSituacao() != null && req.getSituacao().getId() != null) {
                situacaoRepo.findById(req.getSituacao().getId()).ifPresent(m::setSituacao);
            }

            if (req.getObservacoes() != null) m.setObservacoes(req.getObservacoes());

            Matricula salvo = matriculaRepo.save(m);

            // 🔄 Reaplica no perfil do aluno (caso turma/situação tenham mudado)
            aplicarMatriculaNoPerfilAluno(salvo);

            return ResponseEntity.ok(salvo);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        Optional<Matricula> opt = matriculaRepo.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();

        Matricula existente = opt.get();
        Long alunoId = existente.getAluno() != null ? existente.getAluno().getId() : null;

        matriculaRepo.deleteById(id);

        // 🔄 Se o aluno estava "apontando" para esta matrícula, ajusta/limpa
        if (alunoId != null) {
            aposExcluirMatricula(existente);
        }

        return ResponseEntity.noContent().build();
    }

    /* ================= HELPERs ================= */

    /** Aplica/retira a matrícula no perfil do aluno conforme situação. */
    private void aplicarMatriculaNoPerfilAluno(Matricula m) {
        if (m.getAluno() == null) return;

        Aluno a = alunoRepo.findById(m.getAluno().getId()).orElse(null);
        if (a == null) return;

        boolean ativa = m.getSituacao() != null &&
                "Ativa".equalsIgnoreCase(m.getSituacao().getDescricao());

        if (ativa) {
            // Coloca turma (nome) e id da matrícula
            a.setTurma(m.getTurma() != null ? m.getTurma().getNome() : null);
            a.setMatricula(m.getId() != null ? String.valueOf(m.getId()) : null);
            alunoRepo.save(a);
            return;
        }

        // Se não está ativa e o aluno aponta para esta matrícula, tenta achar outra ATIVA
        if (m.getId() != null && String.valueOf(m.getId()).equals(a.getMatricula())) {
            matriculaRepo.findFirstByAluno_IdAndSituacao_DescricaoIgnoreCaseOrderByDataMatriculaDesc(
                            a.getId(), "Ativa")
                    .ifPresentOrElse(ativaOutra -> {
                        a.setTurma(ativaOutra.getTurma() != null ? ativaOutra.getTurma().getNome() : null);
                        a.setMatricula(String.valueOf(ativaOutra.getId()));
                        alunoRepo.save(a);
                    }, () -> {
                        a.setTurma(null);
                        a.setMatricula(null);
                        alunoRepo.save(a);
                    });
        }
    }

    /** Ao excluir uma matrícula, se o aluno apontava para ela, reatribui outra ATIVA ou limpa. */
    private void aposExcluirMatricula(Matricula excluida) {
        if (excluida.getAluno() == null) return;
        Aluno a = alunoRepo.findById(excluida.getAluno().getId()).orElse(null);
        if (a == null) return;

        if (excluida.getId() != null && String.valueOf(excluida.getId()).equals(a.getMatricula())) {
            matriculaRepo.findFirstByAluno_IdAndSituacao_DescricaoIgnoreCaseOrderByDataMatriculaDesc(
                            a.getId(), "Ativa")
                    .ifPresentOrElse(ativaOutra -> {
                        a.setTurma(ativaOutra.getTurma() != null ? ativaOutra.getTurma().getNome() : null);
                        a.setMatricula(String.valueOf(ativaOutra.getId()));
                        alunoRepo.save(a);
                    }, () -> {
                        a.setTurma(null);
                        a.setMatricula(null);
                        alunoRepo.save(a);
                    });
        }
    }
}
