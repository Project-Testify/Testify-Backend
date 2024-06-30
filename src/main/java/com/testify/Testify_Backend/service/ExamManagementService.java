package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.requests.exam_management.ExamRequest;
import com.testify.Testify_Backend.requests.exam_management.QuestionRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;

public interface ExamManagementService {
    void createExam(ExamRequest examRequest);

    GenericAddOrUpdateResponse<QuestionRequest> addQuestion(long examId, QuestionRequest questionRequest);


}
