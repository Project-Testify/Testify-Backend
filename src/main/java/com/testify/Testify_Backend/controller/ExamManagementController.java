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
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<GenericAddOrUpdateResponse> addOrUpdateProctors(@PathVariable Long examId, @RequestBody List<String> emails) {
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

    @PutMapping("/{examId}/real-time-monitoring")
    public ResponseEntity<GenericResponse> updateRealTimeMonitoring(
            @PathVariable Long examId,
            @RequestBody RealTimeMonitoringRequest dto) {
        try {
            // Delegate to the service and return the response directly
            GenericResponse response = examManagementService.updateRealTimeMonitoring(examId, dto);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new GenericResponse("false", "Error: " + ex.getMessage()));
        }
    }

    @GetMapping("/{examId}/real-time-monitoring")
    public ResponseEntity<RealTimeMonitoringResponse> getRealTimeMonitoringStatus(@PathVariable Long examId) {
        try {
            RealTimeMonitoringResponse response = examManagementService.getRealTimeMonitoringStatus(examId);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{examId}/browser-lockdown")
    public ResponseEntity<GenericResponse> updateBrowserLockdown(
            @PathVariable Long examId,
            @RequestParam boolean browserLockdown) {
        try {
            GenericResponse response = examManagementService.updateBrowserLockdown(examId, browserLockdown);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new GenericResponse("false", "Error: " + ex.getMessage()));
        }
    }

    @GetMapping("/{examId}/browser-lockdown")
    public ResponseEntity<BrowserLockdownResponse> getBrowserLockdown(@PathVariable Long examId) {
        try {
            boolean browserLockdown = examManagementService.getBrowserLockdownStatus(examId);
            return ResponseEntity.ok(new BrowserLockdownResponse(browserLockdown));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null); // Handle errors gracefully
        }
    }

    @PutMapping("/{examId}/hosted")
    public ResponseEntity<GenericResponse> updateHosted(@PathVariable Long examId, @RequestParam boolean hosted) {
        try {
            GenericResponse response = examManagementService.updateHostedStatus(examId, hosted);
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(new GenericResponse("false", "Error: " + ex.getMessage()));
        }
    }

    @GetMapping("/{examId}/hosted")
    public ResponseEntity<HostedResponse> getHosted(@PathVariable Long examId) {
        try {
            boolean hosted = examManagementService.getHostedStatus(examId);
            log.info(String.valueOf(hosted));
            return ResponseEntity.ok(new HostedResponse(hosted));
        } catch (Exception ex) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/{examId}/set-moderator")
    public ResponseEntity<String> setModerator(@PathVariable Long examId, @RequestBody ModeratorRequest moderatorRequest) {
        log.info("Setting moderator for examId: " + examId);
        try {
            examManagementService.assignModerator(examId, moderatorRequest.getModeratorEmail());
            return ResponseEntity.ok("Moderator assigned successfully.");
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping("/{examId}/moderator")
    public ResponseEntity<ModeratorResponse> getModerator(@PathVariable Long examId) {
        ModeratorResponse moderatorResponse = examManagementService.getModeratorDetails(examId);
        return ResponseEntity.ok(moderatorResponse); // Returns null in the body if no moderator exists
    }

}
