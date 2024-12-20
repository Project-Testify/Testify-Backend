package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.requests.exam_management.CandidateExamDetailsDTO;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateResponse;
import com.testify.Testify_Backend.responses.candidate_management.CandidateProfile;
import com.testify.Testify_Backend.responses.candidate_management.OrganizationCandidateView;

import java.util.List;

public interface CandidateService {

    List<CandidateResponse> getAllCandidatesForSearch();
//    temp comment
    public List<CandidateExam> getCandidateExams(String status);
    public CandidateExam getCandidateExamDetails(Integer examId);
    public List<OrganizationCandidateView> getOrganizations();
    public CandidateProfile getCandidateProfile();
    public String updateCandidateProfile(Candidate candidate);
    public String deleteCandidateProfile(long id);
    List<CandidateExamDetailsDTO> getCandidateExamDetails(String candidateId);
}
