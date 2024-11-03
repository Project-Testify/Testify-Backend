package com.testify.Testify_Backend.responses.exam_management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GradeResponse {
    private long id;
    private String grade;
    private int minMarks;
    private int maxMarks;
}
