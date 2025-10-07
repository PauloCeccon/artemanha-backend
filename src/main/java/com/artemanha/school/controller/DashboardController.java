package com.artemanha.school.controller;

import com.artemanha.school.repository.AlunoRepository;
import com.artemanha.school.repository.TurmaRepository;
import com.artemanha.school.repository.MatriculaRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

        // ✅ 1. Total de turmas vigentes
        long turmasVigentes = turmaRepo.countBySituacaoIgnoreCase("Vigente");

        // ✅ 2. Total de alunos ativos (status_id = 1)
        long alunosAtivos = alunoRepo.countByStatus_Id(1L);

        // ✅ 3. Total de matrículas ativas (situacao_id = 1)
        long alunosMatriculados = matriculaRepo.countBySituacao_Id(1L);

        // ✅ 4. Média de alunos por turma vigente
        long totalTurmasVigentes = turmaRepo.countBySituacaoIgnoreCase("Vigente");
        double mediaPorTurma = (totalTurmasVigentes > 0)
                ? (double) alunosMatriculados / totalTurmasVigentes
                : 0.0;

        // ✅ 5. Novos alunos criados nos últimos 365 dias
        LocalDateTime umAnoAtras = LocalDateTime.now().minusDays(365);
        long novosAlunosUltimoAno = alunoRepo.countByDataCriacaoAfter(umAnoAtras);

        // 🧩 Monta o JSON de resposta
        dados.put("turmasVigentes", turmasVigentes);
        dados.put("alunosAtivos", alunosAtivos);
        dados.put("alunosMatriculados", alunosMatriculados);
        dados.put("mediaAlunosPorTurma", Math.round(mediaPorTurma * 10.0) / 10.0);
        dados.put("novosAlunosUltimoAno", novosAlunosUltimoAno);

        return dados;
    }
}
