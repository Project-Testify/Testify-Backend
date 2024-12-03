package com.testify.Testify_Backend.responses.exam_management;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CandidateExamAnswerResponse {
    private Long examId;
    private List<AnswerDetail> answers;

    @Data
    @AllArgsConstructor
    public static class AnswerDetail {
        private Long questionId;
        private String questionType;
        private Object answer;
    }
}
