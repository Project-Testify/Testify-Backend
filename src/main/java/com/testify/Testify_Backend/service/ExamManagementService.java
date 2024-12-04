package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.exam_management.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExamManagementService {
    GenericAddOrUpdateResponse<ExamRequest> createExam(ExamRequest examRequest);
    GenericAddOrUpdateResponse<ExamUpdateRequest> updateExam(long examId, ExamUpdateRequest examUpdateRequest);
    ExamResponse getExamById(long examId);
    ExamSessionResponse startExam(StartExamRequest request);
    void saveAnswer(Long sessionId, Long questionId, Long optionId, String answerText);
    void markSessionAsComplete(Long sessionId);
    ResponseEntity<QuestionListResponse> getAllQuestionsAnswersByExamId(long examId, long sessionId);
    CandidateExamAnswerResponse getCandidateAnswers(Long candidateId);

    GenericAddOrUpdateResponse<MCQUpdateRequest> updateMCQQuestion(MCQUpdateRequest mcqUpdateRequest);
    GenericAddOrUpdateResponse<MCQRequest> saveMCQ(MCQRequest mcqRequest);
    GenericAddOrUpdateResponse<EssayRequest> saveEssay(EssayRequest essayRequest);
    GenericAddOrUpdateResponse<EssayUpdateRequest> updateEssayQuestion(EssayUpdateRequest essayUpdateRequest);
    ResponseEntity<QuestionListResponse> getAllQuestionsByExamId(long examId);
    ResponseEntity<GenericDeleteResponse<Void>> deleteQuestion(long questionId);
    ResponseEntity<QuestionResponse> getQuestionById(long questionId);

    GenericAddOrUpdateResponse<QuestionSequenceRequest> updateQuestionSequence(long examId, QuestionSequenceRequest questionSequenceRequest);
    ResponseEntity<QuestionSequenceResponse> getQuestionSequence(long examId);

    ResponseEntity<GenericAddOrUpdateResponse> saveGrades(Long examId, List<GradeRequest> gradeRequestList);
    ResponseEntity<List<GradeResponse>> getGradesByExamId(Long examId);
    ResponseEntity<GenericAddOrUpdateResponse> updateGrades(Long examId, List<GradeRequest> gradeRequestList);

    GenericAddOrUpdateResponse<OrderChangeRequest> updateOrder(long examId, OrderChangeRequest orderRequest);
    ResponseEntity<OrderResponse> getExamOrderTypeAndValue(Long examId);

    ResponseEntity<GenericAddOrUpdateResponse> addProctorsToExam(long examId, List<String> proctorEmails);
    ResponseEntity<List<ProctorResponse>> getProctorsByExamId(Long examId);

    ResponseEntity<GenericAddOrUpdateResponse<CandidateEmailListRequest>> updateExamCandidates(Long examId, List<String> candidateEmails);
    List<CandidateResponse> getCandidatesByExamId(Long examId);

    List<CandidateGroupSearchResponse> getCandidateGroupsByOrganizationForSearch(Long organizationId);


    GenericAddOrUpdateResponse<CandidateEmailListRequest> addCandidatesToExam(long examId, CandidateEmailListRequest request);

    List<ConflictExamResponse> getExamsScheduledBetween(Long examId);

    List<CandidateConflictExamResponse> getCandidateConflictingExams(Long examId);

    GenericResponse updateRealTimeMonitoring(Long examId, RealTimeMonitoringRequest dto);
    RealTimeMonitoringResponse getRealTimeMonitoringStatus(Long examId);

    GenericResponse updateBrowserLockdown(Long examId, boolean browserLockdown);
    boolean getBrowserLockdownStatus(Long examId);

    GenericResponse updateHostedStatus(Long examId, boolean hosted);
    boolean getHostedStatus(Long examId);

    void assignModerator(Long examId, String moderatorEmail);
    ModeratorResponse getModeratorDetails(Long examId);

    GenericAddOrUpdateResponse<QuestionCommentRequest> updateQuestionComment(Long questionId, String comment);
}
