package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Grade;
import com.testify.Testify_Backend.responses.EssayDetailsResponse;
import com.testify.Testify_Backend.service.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
