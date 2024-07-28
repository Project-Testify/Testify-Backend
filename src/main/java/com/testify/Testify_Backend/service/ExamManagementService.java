package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.requests.exam_management.CandidateEmailListRequest;
import com.testify.Testify_Backend.requests.exam_management.ExamRequest;
import com.testify.Testify_Backend.requests.exam_management.QuestionRequest;
import com.testify.Testify_Backend.requests.exam_management.QuestionSequenceRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import org.springframework.http.ResponseEntity;

public interface ExamManagementService {
    GenericAddOrUpdateResponse<ExamRequest> createExam(ExamRequest examRequest);
    GenericAddOrUpdateResponse<QuestionRequest> addQuestion(long examId, QuestionRequest questionRequest);
    GenericAddOrUpdateResponse<QuestionSequenceRequest> updateQuestionSequence(long examId, QuestionSequenceRequest questionSequenceRequest);
    ResponseEntity<Exam> getExamResponse(long examId);
    GenericAddOrUpdateResponse<CandidateEmailListRequest> addCandidatesToExam(long examId, CandidateEmailListRequest request);
}
