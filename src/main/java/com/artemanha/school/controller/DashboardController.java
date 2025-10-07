package com.artemanha.school.controller;

import com.artemanha.school.repository.AlunoRepository;
import com.artemanha.school.repository.TurmaRepository;
import com.artemanha.school.repository.MatriculaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
public class DashboardController {

    private final TurmaRepository turmaRepo;
    private final AlunoRepository alunoRepo;
    private final MatriculaRepository matriculaRepo;

    public DashboardController(TurmaRepository turmaRepo,
                               AlunoRepository alunoRepo,
                               MatriculaRepository matriculaRepo) {
        this.turmaRepo = turmaRepo;
        this.alunoRepo = alunoRepo;
        this.matriculaRepo = matriculaRepo;
    }

    @GetMapping
    public Map<String, Object> getDashboardData() {
        Map<String, Object> dados = new HashMap<>();

        long turmasVigentes = turmaRepo.countBySituacaoIgnoreCase("Vigente");
        long alunosAtivos = alunoRepo.countByStatus_Id(1L);
        long matriculasAtivas = matriculaRepo.countBySituacao_Id(1L);

        double mediaPorTurma = (turmasVigentes > 0)
                ? (double) matriculasAtivas / turmasVigentes
                : 0.0;

        dados.put("turmasVigentes", turmasVigentes);
        dados.put("alunosAtivos", alunosAtivos);
        dados.put("alunosMatriculados", matriculasAtivas); // 👈 corrigido aqui
        dados.put("mediaAlunosPorTurma", Math.round(mediaPorTurma * 10.0) / 10.0);

        return dados;
    }

}
