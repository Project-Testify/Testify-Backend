package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;

import java.util.List;

public interface CandidateService {
    public List<CandidateExam> getCandidateExams();
}
