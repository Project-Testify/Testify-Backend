package com.testify.Testify_Backend.responses.exam_management;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionResponse {
    private long questionId;
    private String questionText;
    private String questionType; // "MCQ" or "Essay"
    private String difficultyLevel; // "EASY", "MEDIUM" or "HARD"
    private List<MCQOptionResponse> options; // List of options for MCQs
    private List<EssayCoverPointResponse> coverPoints; // List of cover points for essays
}
