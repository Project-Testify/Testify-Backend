package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.requests.exam_management.CandidateEmailListRequest;
import com.testify.Testify_Backend.requests.exam_management.ExamRequest;
import com.testify.Testify_Backend.requests.exam_management.QuestionRequest;
import com.testify.Testify_Backend.requests.exam_management.QuestionSequenceRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.service.ExamManagementService;
import com.testify.Testify_Backend.service.ExamManagementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamManagementController {
    private final ExamManagementService examManagementService;

    @PostMapping
    public GenericAddOrUpdateResponse<ExamRequest> createExam(@RequestBody ExamRequest examRequest){
        return examManagementService.createExam(examRequest);
    }

    @PostMapping("/{examId}/addQuestion")
    public GenericAddOrUpdateResponse<QuestionRequest> addQuestionToExam(@PathVariable long examId, @RequestBody QuestionRequest questionRequest){
        return examManagementService.addQuestion(examId, questionRequest);
    }

    @PutMapping("/{examId}/updateQuestionSequence")
    public GenericAddOrUpdateResponse<QuestionSequenceRequest> updateQuestionSequence(@PathVariable long examId, @RequestBody QuestionSequenceRequest questionSequenceRequest) {
        return examManagementService.updateQuestionSequence(examId, questionSequenceRequest);
    }

    @GetMapping("/{examId}")
    public ResponseEntity<Exam> getExam(@PathVariable long examId){
        return examManagementService.getExamResponse(examId);
    }

    @PostMapping("/{examId}/addCandidates")
    public ResponseEntity<GenericAddOrUpdateResponse<CandidateEmailListRequest>> addCandidatesToExam(
            @PathVariable long examId, @RequestBody CandidateEmailListRequest request) {
        GenericAddOrUpdateResponse<CandidateEmailListRequest> response = examManagementService.addCandidatesToExam(examId, request);
        return ResponseEntity.ok(response);
    }

//    @GetMapping("/{examId}")
//    public Exam getExam(@PathVariable long examId){
//        return examManagementService.getExam(examId);
//    }
}
