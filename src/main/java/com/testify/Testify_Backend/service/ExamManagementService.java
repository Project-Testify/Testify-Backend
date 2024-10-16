package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import com.testify.Testify_Backend.responses.exam_management.QuestionListResponse;
import com.testify.Testify_Backend.responses.exam_management.QuestionResponse;
import com.testify.Testify_Backend.responses.exam_management.QuestionSequenceResponse;
import org.springframework.http.ResponseEntity;

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

    GenericAddOrUpdateResponse<OrderChangeRequest> updateOrder(long examId, OrderChangeRequest orderRequest);


    GenericAddOrUpdateResponse<CandidateEmailListRequest> addCandidatesToExam(long examId, CandidateEmailListRequest request);
}
