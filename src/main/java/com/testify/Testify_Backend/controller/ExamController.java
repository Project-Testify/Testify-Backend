package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.requests.auth.ExamRequest;
import com.testify.Testify_Backend.requests.auth.QuestionRequest;
import com.testify.Testify_Backend.service.ExamCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamController {
    private final ExamCreationService examService;

    @PostMapping("/create")
    public String createExam(@RequestBody ExamRequest examRequest){
        examService.createExam(examRequest);
        return "Exam Created Successfully";
    }

    @PostMapping("/{examId}/questions")
    public String addQuestionsToExam(@PathVariable long examId, @RequestBody Set<QuestionRequest> questionRequests){
        examService.addQuestionsToExam(examId, questionRequests);
        return "Questions Added Successfully";
    }
}
