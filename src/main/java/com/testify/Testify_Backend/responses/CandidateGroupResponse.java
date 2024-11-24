package com.testify.Testify_Backend.responses;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.responses.candidate_management.CandidateResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CandidateGroupResponse {
    private long id;
    private String name;
    private Set<CandidateResponse> candidates;
}
