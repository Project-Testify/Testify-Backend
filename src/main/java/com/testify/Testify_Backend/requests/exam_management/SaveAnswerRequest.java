package com.testify.Testify_Backend.requests.exam_management;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaveAnswerRequest {

    private Long sessionId;
    private Long questionId;
    private Long optionId;  // For MCQ
    private String answerText;  // For Essay questions
}
