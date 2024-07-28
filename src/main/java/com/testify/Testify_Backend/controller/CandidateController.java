package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping("/candidateExams")
    public ResponseEntity<List<CandidateExam>> getCandidateExams() {
        List<CandidateExam> candidateExams = candidateService.getCandidateExams();
        return ResponseEntity.ok(candidateExams);
    }

}
