package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.service.CandidateService;
import com.testify.Testify_Backend.service.CandidateServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping
    public ResponseEntity<Candidate> getCandidateById(@PathVariable long candidateId) {
        Candidate candidate = candidateService.getCandidateById(candidateId);
        return ResponseEntity.ok(candidate);
    }
}
