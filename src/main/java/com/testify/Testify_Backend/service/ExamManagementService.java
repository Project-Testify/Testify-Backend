package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.exam_management.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ExamManagementService {
    GenericAddOrUpdateResponse<ExamRequest> createExam(ExamRequest examRequest);
    GenericAddOrUpdateResponse<ExamUpdateRequest> updateExam(long examId, ExamUpdateRequest examUpdateRequest);
    ExamResponse getExamById(long examId);

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


    GenericAddOrUpdateResponse<CandidateEmailListRequest> addCandidatesToExam(long examId, CandidateEmailListRequest request);
}
