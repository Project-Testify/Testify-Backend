package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.repository.CandidateRepository;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateProfile;
import com.testify.Testify_Backend.utils.UserUtil;
import com.testify.Testify_Backend.utils.VarList;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CandidateExam> getCandidateExams(){
//        String currentUserEmail = UserUtil.getCurrentUserName();
        String currentUserEmail = "john.doe@example.com";
        Candidate candidate = candidateRepository.findByEmail(currentUserEmail).get();

        return candidate.getExams().stream().map(exam -> modelMapper.map(exam, CandidateExam.class)).toList();
    }

    @Override
    public CandidateProfile getCandidateProfile(){
        String currentUserEmail = "john.doe@example.com";
        Candidate candidate = candidateRepository.findByEmail(currentUserEmail).get();

        return modelMapper.map(candidate, CandidateProfile.class);
    }

    @Override
    public String updateCandidateProfile(Candidate candidate){
        if(candidateRepository.existsByEmail(candidate.getEmail())){
            candidateRepository.save(candidate);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    @Override
    public String deleteCandidateProfile(long id){
        if(candidateRepository.existsById(id)){
            candidateRepository.deleteById(id);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }



}