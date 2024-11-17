package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.enums.ExamStatus;
import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.repository.CandidateRepository;
import com.testify.Testify_Backend.repository.ExamRepository;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateProfile;
import com.testify.Testify_Backend.utils.UserUtil;
import com.testify.Testify_Backend.utils.VarList;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private final CandidateRepository candidateRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final ExamRepository examRepository;
    @Autowired
    private GenericResponse genericResponse;

    @Override
    public List<CandidateExam> getCandidateExams(String status) {
        // Get the current user's email (you can adapt this to your actual method of getting the logged-in user's email)
        String currentUserEmail = "testcan@gmail.com";
        Candidate candidate = candidateRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found"));

        // Get exams directly associated with the candidate
        List<Exam> candidateExams = examRepository.findExamsByCandidateId(candidate.getId());

        // Get all public exams
        List<Exam> publicExams = examRepository.findAllPublicExams();

        // Combine both lists, ensuring no duplicates
        Set<Exam> allExams = new HashSet<>(candidateExams);
        allExams.addAll(publicExams);

        if (allExams.isEmpty()) {
            return new ArrayList<>();
        } else {
            LocalDateTime now = LocalDateTime.now();

            // Filter exams based on the provided status
            return allExams.stream()
                    .map(exam -> {
                        CandidateExam candidateExam = modelMapper.map(exam, CandidateExam.class);

                        if (now.isBefore(exam.getStartDatetime())) {
                            candidateExam.setStatus(ExamStatus.UPCOMING);
                        } else if (now.isAfter(exam.getEndDatetime())) {
                            candidateExam.setStatus(ExamStatus.EXPIRED);
                        } else {
                            candidateExam.setStatus(ExamStatus.ONGOING);
                        }

                        return candidateExam;
                    })
                    .filter(candidateExam -> {
                        // Apply the status filter
                        if (status == null) {
                            return true;
                        }
                        return candidateExam.getStatus().name().equalsIgnoreCase(status);
                    })
                    .collect(Collectors.toList());
        }
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