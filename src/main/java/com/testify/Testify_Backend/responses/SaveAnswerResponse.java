package com.testify.Testify_Backend.responses;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SaveAnswerResponse {
    private Long sessionId;
    private Long candidateId;
    private List<AnswerDTO> answers;

    @Getter
    @Setter
    public static class AnswerDTO {
        private Long questionId;
        private Long optionId; // For MCQ
        private String answerText; // For Essay questions
    }
}

