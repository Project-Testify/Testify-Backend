package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateResponse;

import java.util.List;

public interface CandidateService {
    List<CandidateExam> getCandidateExams();

    List<CandidateResponse> getAllCandidatesForSearch();

//    temp comment
}
