package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.repository.CandidateRepository;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateProfile;
import com.testify.Testify_Backend.service.CandidateService;
import com.testify.Testify_Backend.utils.VarList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/v1/candidate")
@RequiredArgsConstructor
public class CandidateController {
    private final CandidateService candidateService;

    @GetMapping("/exams")
    public ResponseEntity<List<CandidateExam>> getCandidateExams(@RequestParam(value = "status", required = false) String status) {
        List<CandidateExam> candidateExams;
        if (status != null) {
            candidateExams = candidateService.getCandidateExams(status);
        } else {
            candidateExams = candidateService.getCandidateExams(null);
        }
        return ResponseEntity.ok(candidateExams);
    }


    @GetMapping("/profile")
    public ResponseEntity<CandidateProfile> getCandidateProfile() {
        CandidateProfile candidateProfile = candidateService.getCandidateProfile();
        return ResponseEntity.ok(candidateProfile);
    }

    @PutMapping("/profile/update")
    public ResponseEntity updateCandidateProfile(@RequestBody Candidate candidate) {
        try {
            String response = candidateService.updateCandidateProfile(candidate);
            if (response.equals("01")) {
                return new ResponseEntity<>("Success", HttpStatus.OK);
            } else if (response.equals("02")) {
                return new ResponseEntity<>("Does not Exist", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Something Went Wrong", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Something Went Wrong" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/profile/delete/{id}")
    public ResponseEntity deleteCandidateProfile(long id){
        try{
            String response = candidateService.deleteCandidateProfile(id);
            if (response.equals("01")) {
                return new ResponseEntity<>("Success", HttpStatus.OK);
            } else if (response.equals("02")) {
                return new ResponseEntity<>("Does not Exist", HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>("Something Went Wrong", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Something Went Wrong" + e, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
