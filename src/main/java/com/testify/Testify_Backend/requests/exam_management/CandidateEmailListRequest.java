package com.testify.Testify_Backend.requests.exam_management;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CandidateEmailListRequest {
    private List<String> emails;
}
