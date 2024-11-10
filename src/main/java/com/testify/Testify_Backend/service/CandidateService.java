package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateProfile;

import java.util.List;

public interface CandidateService {
    public List<CandidateExam> getCandidateExams();
    public CandidateProfile getCandidateProfile();
    public String updateCandidateProfile(Candidate candidate);
    public String deleteCandidateProfile(long id);

}
