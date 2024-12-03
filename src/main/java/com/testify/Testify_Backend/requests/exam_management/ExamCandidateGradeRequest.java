package com.testify.Testify_Backend.requests.exam_management;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ExamCandidateGradeRequest
{
    private String examID;
    private String candidateID;
    private String status;
    private String grade;
    private String score;
}
