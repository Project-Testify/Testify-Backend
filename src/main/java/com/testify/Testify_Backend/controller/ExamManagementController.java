package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.exam_management.QuestionSequenceResponse;
import com.testify.Testify_Backend.service.ExamManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamManagementController {
    private final ExamManagementService examManagementService;

    @PostMapping
    public ResponseEntity<GenericAddOrUpdateResponse<ExamRequest>> createExam(@RequestBody ExamRequest examRequest){
        GenericAddOrUpdateResponse<ExamRequest> response = examManagementService.createExam(examRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/{examId}/mcq")
    public GenericAddOrUpdateResponse<MCQRequest> addMCQ(@RequestBody MCQRequest mcqRequest){
        GenericAddOrUpdateResponse<MCQRequest> response = examManagementService.saveMCQ(mcqRequest);
        return response;
    }

    @PutMapping("/{examId}/mcq")
    public GenericAddOrUpdateResponse<MCQUpdateRequest> updateMCQQuestion(@RequestBody MCQUpdateRequest mcqUpdateRequest) {
        return examManagementService.updateMCQQuestion(mcqUpdateRequest);// Return 200 OK response
    }

    @PostMapping("/{examId}/essay")
    public GenericAddOrUpdateResponse<EssayRequest> addEssay(@RequestBody EssayRequest essayRequest){
        GenericAddOrUpdateResponse<EssayRequest> response = examManagementService.saveEssay(essayRequest);
        return response;
    }

    @PutMapping("/{examId}/essay")
    public GenericAddOrUpdateResponse<EssayUpdateRequest> updateEssayQuestion(@RequestBody EssayUpdateRequest essayUpdateRequest) {
        return examManagementService.updateEssayQuestion(essayUpdateRequest);
    }


    @PutMapping("/{examId}/questionSequence")
    public GenericAddOrUpdateResponse<QuestionSequenceRequest> updateQuestionSequence(@PathVariable long examId, @RequestBody QuestionSequenceRequest questionSequenceRequest) {
        return examManagementService.updateQuestionSequence(examId, questionSequenceRequest);
    }

    @GetMapping("/{examId}/questionSequence")
    public ResponseEntity<QuestionSequenceResponse> getQuestionSequence(@PathVariable long examId){
        return examManagementService.getQuestionSequence(examId);
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


}
