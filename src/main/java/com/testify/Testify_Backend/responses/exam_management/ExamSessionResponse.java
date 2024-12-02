package com.testify.Testify_Backend.responses.exam_management;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ExamSessionResponse {
    private Long sessionId;
    private Long candidateId;
    private Long examId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean inProgress;
    private Map<Long, Long> answers;
    private int currentQuestionIndex;
}

