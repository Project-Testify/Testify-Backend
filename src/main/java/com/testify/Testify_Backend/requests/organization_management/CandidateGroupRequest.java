package com.testify.Testify_Backend.requests.organization_management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CandidateGroupRequest {
    private String name;
    private List<Long> candidateIds;
}
