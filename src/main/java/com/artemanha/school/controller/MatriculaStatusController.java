package com.artemanha.school.controller;

import com.artemanha.school.entity.MatriculaStatus;
import com.artemanha.school.repository.MatriculaStatusRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status")
@CrossOrigin(origins = "*")
public class MatriculaStatusController {

    private final MatriculaStatusRepository repo;

    public MatriculaStatusController(MatriculaStatusRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<MatriculaStatus> listar() {
        return repo.findAll();
    }
}