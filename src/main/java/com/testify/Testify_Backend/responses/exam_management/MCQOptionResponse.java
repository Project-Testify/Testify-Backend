package com.testify.Testify_Backend.responses.exam_management;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MCQOptionResponse {
    private long optionId;
    private String optionText;
    private boolean isCorrect;
    private double marks;
}
