package com.testify.Testify_Backend.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class McqDetailsResponse {
    private Long questionId;
    private String questionText;
    private String questionType;
    private String difficultyLevel; // Could be nullable if not required
    private List<OptionResponse> options;
    private Long userAnswer;

    @Data
    @Builder
    public static class OptionResponse {

        private Long optionId;
        private String optionText;
        private boolean correct;
        private Double marks;
    }
}
