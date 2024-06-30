package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.requests.exam_management.ExamRequest;
import com.testify.Testify_Backend.requests.exam_management.QuestionRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.service.ExamManagementService;
import com.testify.Testify_Backend.service.ExamManagementServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/exam")
@RequiredArgsConstructor
public class ExamManagementController {
    private final ExamManagementService examManagementService;

    @PostMapping("/create")
    public String createExam(@RequestBody ExamRequest examRequest){
        examManagementService.createExam(examRequest);
        return "Exam Created Successfully";
    }

    @PostMapping("/{examId}/addQuestion")
    public GenericAddOrUpdateResponse<QuestionRequest> addQuestionToExam(@PathVariable long examId, @RequestBody QuestionRequest questionRequest){
        return examManagementService.addQuestion(examId, questionRequest);
    }



//    @GetMapping("/{examId}")
//    public Exam getExam(@PathVariable long examId){
//        return examManagementService.getExam(examId);
//    }
}
