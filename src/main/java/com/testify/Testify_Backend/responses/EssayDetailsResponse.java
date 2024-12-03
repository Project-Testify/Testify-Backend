package com.testify.Testify_Backend.responses;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EssayDetailsResponse {
    private String questionText;
    private List<CoverPointDto> coverPoints;
    private String userAnswer;

    @Data
    @Builder
    public static class CoverPointDto {
        private String coverPointText;
        private double marks;
    }
}

