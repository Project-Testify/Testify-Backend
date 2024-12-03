package com.testify.Testify_Backend.responses.candidate_management;

import com.testify.Testify_Backend.enums.ExamStatus;
import com.testify.Testify_Backend.enums.ExamType;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.responses.OrgResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CandidateExam {
    private long id;
    private String title;
    private String description;
    private ExamType examType;
    private String startTime;
    private String endTime;
    private int duration;
    private String topics;
    private String instructions;
    private int totalMarks;
    private OrgResponse organization;
    private ExamStatus status;
}
