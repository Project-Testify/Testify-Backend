package com.testify.Testify_Backend.responses.exam_management;

import com.testify.Testify_Backend.enums.ExamType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CandidateExamAnswerResponse {
    private Long examId;
    private String examName;
    private ExamType examType;
    private Long sessionId;
    private LocalDateTime endTime;
    private List<AnswerDetail> answers;

    @Data
    @AllArgsConstructor
    public static class AnswerDetail {
        private Long questionId;
        private String questionType;
        private Object answer;
    }
}
