package com.testify.Testify_Backend.responses.candidate_management;

import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.responses.OrgResponse;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CandidateExam {
    private String title;
    private String startTime;
    private String endTime;
    private int duration;
    private String instructions;
    private int totalMarks;
    private OrgResponse organization;
}
