package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Grade;
import com.testify.Testify_Backend.responses.EssayDetailsResponse;

import java.util.List;

public interface GradingService {
    List<EssayDetailsResponse> getEssayDetails(Long examId, Long userId);
    List<Grade> getGradingSchemeForExam(Long examId);
}
