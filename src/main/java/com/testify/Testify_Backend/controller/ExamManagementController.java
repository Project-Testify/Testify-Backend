package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.model.Grade;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.exam_management.*;
import com.testify.Testify_Backend.service.ExamManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/{examId}")
    public ResponseEntity<GenericAddOrUpdateResponse<ExamUpdateRequest>> updateExam(
            @PathVariable long examId,
            @RequestBody ExamUpdateRequest examUpdateRequest) {
        GenericAddOrUpdateResponse<ExamUpdateRequest> response = examManagementService.updateExam(examId, examUpdateRequest);
        return ResponseEntity.ok(response);
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

    @GetMapping("/{examId}/questions")
    public ResponseEntity<QuestionListResponse> getAllQuestionsByExamId(@PathVariable long examId){
        return examManagementService.getAllQuestionsByExamId(examId);
    }

    @PutMapping("/question/{questionId}")
    public ResponseEntity<GenericDeleteResponse<Void>> deleteQuestion(@PathVariable long questionId){
        return examManagementService.deleteQuestion(questionId);
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<QuestionResponse> getQuestionById(@PathVariable long questionId){
        return examManagementService.getQuestionById(questionId);
    }

    @PostMapping("/{examId}/addCandidates")
    public ResponseEntity<GenericAddOrUpdateResponse<CandidateEmailListRequest>> addCandidatesToExam(
            @PathVariable long examId, @RequestBody CandidateEmailListRequest request) {
        GenericAddOrUpdateResponse<CandidateEmailListRequest> response = examManagementService.addCandidatesToExam(examId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResponse> getExamById(@PathVariable("id") long examId) {
        ExamResponse examResponse = examManagementService.getExamById(examId);
        return ResponseEntity.ok( examResponse);
    }

    @PostMapping("/{examId}/order")
    public ResponseEntity<GenericAddOrUpdateResponse<OrderChangeRequest>> updateOrder(@PathVariable long examId, @RequestBody OrderChangeRequest orderRequest) {
        GenericAddOrUpdateResponse<OrderChangeRequest> response = examManagementService.updateOrder(examId, orderRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{examId}/order")
    public ResponseEntity<OrderResponse> getExamOrderType(@PathVariable Long examId) {
        return examManagementService.getExamOrderTypeAndValue(examId);
    }

    @PostMapping("/{examId}/grades")
    public ResponseEntity<GenericAddOrUpdateResponse> saveGrades(@PathVariable Long examId, @RequestBody List<GradeRequest> gradeRequestList) {
        return examManagementService.saveGrades(examId, gradeRequestList);
    }

    @GetMapping("/{examId}/grades")
    public ResponseEntity<List<GradeResponse>> getGradesByExamId(@PathVariable Long examId) {
        return examManagementService.getGradesByExamId(examId);
    }

    @PutMapping("/{examId}/grades")
    public ResponseEntity<GenericAddOrUpdateResponse> updateGrades(@PathVariable Long examId, @RequestBody List<GradeRequest> gradeRequestList) {
        return examManagementService.updateGrades(examId, gradeRequestList);
    }

}
