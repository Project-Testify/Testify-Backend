package com.testify.Testify_Backend.requests.exam_management;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CandidateExamDetailsDTO {
    private String grade;
    private String score;
    private String examName;
    private String organizationName;
}
