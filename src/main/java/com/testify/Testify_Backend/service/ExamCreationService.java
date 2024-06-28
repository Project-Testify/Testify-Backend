package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.requests.exam.ExamRequest;
import com.testify.Testify_Backend.requests.exam.QuestionRequest;

import java.util.Set;

public interface ExamCreationService {
    public void createExam(ExamRequest examRequest);
    public void addQuestionsToExam(long exam_id, Set<QuestionRequest> questionRequests);

}
