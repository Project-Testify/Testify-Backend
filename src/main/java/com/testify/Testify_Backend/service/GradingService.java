package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.CandidateExamSession;
import com.testify.Testify_Backend.model.ExamCandidateGrade;
import com.testify.Testify_Backend.model.Grade;
import com.testify.Testify_Backend.requests.exam_management.ExamCandidateGradeRequest;
import com.testify.Testify_Backend.responses.EssayDetailsResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamCandidateGradeResponse;

import java.util.List;
import java.util.Map;

public interface GradingService {
    List<EssayDetailsResponse> getEssayDetails(Long examId, Long userId);
    List<Grade> getGradingSchemeForExam(Long examId);
    List<Map<String, String>> getQuestionAndOptionBySessionId(Long sessionId);

    String setExamCandidateGrade(ExamCandidateGradeRequest examCandidateGradeRequest);

    List<ExamCandidateGradeResponse> getExamCandidateGrade();
}
