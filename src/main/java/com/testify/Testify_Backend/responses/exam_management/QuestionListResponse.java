package com.testify.Testify_Backend.responses.exam_management;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionListResponse {
    private long examId;
    private List<QuestionResponse> questions; // List of QuestionResponse objects
    private String errorMessage; // Optional error message if any
}
