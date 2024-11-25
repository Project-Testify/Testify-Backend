package com.testify.Testify_Backend.requests.exam_management;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class GradeRequest {
    private String gradingString;
    private int minMarks;
    private int maxMarks;
}
