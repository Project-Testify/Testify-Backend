package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.repository.CandidateRepository;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateResponse;
import com.testify.Testify_Backend.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CandidateExam> getCandidateExams(){
        String currentUserEmail = UserUtil.getCurrentUserName();
        Candidate candidate = candidateRepository.findByEmail(currentUserEmail).get();

        return candidate.getExams().stream().map(exam -> modelMapper.map(exam, CandidateExam.class)).toList();
    }

    public List<CandidateResponse> getAllCandidatesForSearch() {
        List<Candidate> candidates = candidateRepository.findAll();

        if (candidates.isEmpty()) {
            return null;
        }

        return candidates.stream()
                .map(candidate -> new CandidateResponse(
                        candidate.getId(),
                        candidate.getFirstName(),
                        candidate.getLastName(),
                        candidate.getEmail()
                        ))
                .collect(Collectors.toList());
    }
}