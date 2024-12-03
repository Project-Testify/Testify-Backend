package com.testify.Testify_Backend.responses.exam_management;


import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.model.Exam;
import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamCandidateGradeResponse {

    private String examID;
    private String examTitle;
    private String candidateName;
    private String candidateID;
    private String status;
    private String grade;
    private String score;
}
