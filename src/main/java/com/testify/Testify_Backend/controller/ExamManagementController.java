package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.SaveAnswerResponse;
import com.testify.Testify_Backend.responses.exam_management.*;
import com.testify.Testify_Backend.service.ExamManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.exam_management.*;
import com.testify.Testify_Backend.service.ExamManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
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
        return examManagementService.updateMCQQuestion(mcqUpdateRequest);
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

    @GetMapping("/{examId}/questions/{sessionId}/answers")
    public ResponseEntity<QuestionListResponse> getAllQuestionsAnswersByExamId(
            @PathVariable long examId,
            @PathVariable long sessionId) {

        return examManagementService.getAllQuestionsAnswersByExamId(examId, sessionId);
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

    @PostMapping("/start")
    public ResponseEntity<ExamSessionResponse > startExam(@RequestBody StartExamRequest request) {
        ExamSessionResponse  session = examManagementService.startExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(session);
    }

    @PostMapping("/save-answer")
    public ResponseEntity<String> saveAnswer(@RequestBody SaveAnswerRequest request) {
        try {
            // Call the service method to save the answer
            examManagementService.saveAnswer(
                    request.getSessionId(),
                    request.getQuestionId(),
                    request.getOptionId(),
                    request.getAnswerText()
            );
            return new ResponseEntity<>("Answer saved successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error saving answer: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{sessionId}/submit")
    public ResponseEntity<String> submitExam(@PathVariable Long sessionId) {
        examManagementService.markSessionAsComplete(sessionId);
        return ResponseEntity.ok("Exam submitted successfully.");
    }
    
    @PostMapping("/{examId}/proctors")
    public ResponseEntity<GenericAddOrUpdateResponse> addOrUpdateProctors(
            @PathVariable Long examId,
            @RequestBody List<String> emails) {
        log.info("Adding proctors to examId: " + examId);
        log.info("Emails: " + emails);
        return examManagementService.addProctorsToExam(examId, emails);
    }

    @GetMapping("/{examId}/proctors")
    public ResponseEntity<List<ProctorResponse>> getProctorsByExamId(@PathVariable Long examId) {
        return examManagementService.getProctorsByExamId(examId);
    }

    @PostMapping("/{examId}/update-candidates")
    public ResponseEntity<GenericAddOrUpdateResponse<CandidateEmailListRequest>> updateExamCandidates(
            @PathVariable Long examId,
            @RequestBody CandidateEmailListRequest candidateEmailListRequest) {

        log.info("Updating candidates for examId: " + examId);
        log.info("Emails: " + candidateEmailListRequest.getEmails());
        return examManagementService.updateExamCandidates(examId, candidateEmailListRequest.getEmails());
    }

    @GetMapping("/{examId}/candidates")
    public ResponseEntity<List<CandidateResponse>> getExamCandidates(@PathVariable Long examId) {
        List<CandidateResponse> candidates = examManagementService.getCandidatesByExamId(examId);
        return ResponseEntity.ok(candidates);
    }
    @GetMapping("/{examId}/conflicting-exams")
    public ResponseEntity<List<ConflictExamResponse>> getConflictingExams(@PathVariable Long examId) {
        List<ConflictExamResponse> conflictingExams = examManagementService.getExamsScheduledBetween(examId);
        return ResponseEntity.ok(conflictingExams);
    }

    @GetMapping("/{examId}/conflicting-candidates")
    public ResponseEntity<List<CandidateConflictExamResponse>> getCandidateConflictingExams(@PathVariable Long examId) {
        List<CandidateConflictExamResponse> response = examManagementService.getCandidateConflictingExams(examId);
        return ResponseEntity.ok(response);
    }

}
