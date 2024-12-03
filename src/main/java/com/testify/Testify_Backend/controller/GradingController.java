package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.CandidateExamSession;
import com.testify.Testify_Backend.model.Grade;
import com.testify.Testify_Backend.requests.exam_management.ExamCandidateGradeRequest;
import com.testify.Testify_Backend.responses.EssayDetailsResponse;
import com.testify.Testify_Backend.responses.McqDetailsResponse;
import com.testify.Testify_Backend.service.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/grade")
@RequiredArgsConstructor
public class GradingController {

    private final GradingService gradingService;

    @GetMapping("/{examId}/users/{userId}/essay-details")
    public ResponseEntity<List<EssayDetailsResponse>> getEssayDetails(
            @PathVariable Long examId,
            @PathVariable Long userId) {
        List<EssayDetailsResponse> response = gradingService.getEssayDetails(examId, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{examId}/grading-scheme")
    public ResponseEntity<List<Grade>> getGradingScheme(@PathVariable Long examId) {
        List<Grade> grades = gradingService.getGradingSchemeForExam(examId);
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/{sessionId}/mcq-details")
    public ResponseEntity<List<Map<String, String>>> getResultsBySessionId(
            @PathVariable Long sessionId) {

        List<Map<String, String>> results = gradingService.getQuestionAndOptionBySessionId(sessionId);

        return ResponseEntity.ok(results);
    }

    @PostMapping("/setExamCandidateGrade")
    public ResponseEntity<String> setExamCandidateGrade(@RequestBody ExamCandidateGradeRequest examCandidateGradeRequest) {
        String response = gradingService.setExamCandidateGrade(examCandidateGradeRequest);
        return ResponseEntity.ok(response);
    }

}
