package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.requests.exam_management.ExamCandidateGradeRequest;
import com.testify.Testify_Backend.responses.EssayDetailsResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamCandidateGradeResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradingServiceImpl implements GradingService {

    private final QuestionRepository questionRepository;
    private final CandidateExamAnswerRepository candidateExamAnswerRepository;
    private final GradeRepository gradeRepository;
    private final ExamSessionRepository examSessionRepository;
    private final ExamCandidateGradeRepository examCandidateGradeRepository;
    private final ExamRepository examRepository;
    private final CandidateRepository candidateRepository;

    @Override
    @Transactional
    public List<EssayDetailsResponse> getEssayDetails(Long examId, Long userId) {
        // Fetch all essay questions for the exam
        List<Essay> essayQuestions = questionRepository.findAllEssaysByExamId(examId);
        if (essayQuestions.isEmpty()) {
            throw new IllegalArgumentException("No essay questions found for the given exam ID");
        }

        // Fetch the candidate's session for the exam
        CandidateExamSession candidateExamSession = examSessionRepository.findByExamIdAndCandidateId(examId, userId)
                .orElseThrow(() -> new IllegalArgumentException("No session found"));

        // Process each essay question
        return essayQuestions.stream().map(essayQuestion -> {
            // Fetch the candidate's answer for the essay question
            CandidateExamAnswer candidateAnswer = candidateExamAnswerRepository
                    .findByCandidateExamSessionIdAndQuestionId(candidateExamSession.getId(), essayQuestion.getId());

            String answerText = "";
            if(candidateAnswer != null) {
                answerText = candidateExamAnswerRepository.findEssayAnswerTextById(candidateAnswer.getId());
            }

            // Map the cover points
            List<EssayDetailsResponse.CoverPointDto> coverPointDtos = essayQuestion.getCoverPoints().stream()
                    .map(cp -> EssayDetailsResponse.CoverPointDto.builder()
                            .coverPointText(cp.getCoverPointText())
                            .marks(cp.getMarks())
                            .build())
                    .collect(Collectors.toList());

            // Build the response for this essay question
            return EssayDetailsResponse.builder()
                    .questionText(essayQuestion.getQuestionText())
                    .coverPoints(coverPointDtos)
                    .userAnswer(answerText)
                    .build();
        }).collect(Collectors.toList());
    }


    @Override
    @Transactional
    public List<Grade> getGradingSchemeForExam(Long examId) {
        List<Grade> grades = gradeRepository.findByExamId(examId);

        if (grades == null || grades.isEmpty()) {
            throw new IllegalArgumentException("Grading scheme not found for exam ID: " + examId);
        }

        return grades;
    }

    @Override
    @Transactional
    public List<Map<String, String>> getQuestionAndOptionBySessionId(Long sessionId) {
        List<CandidateExamAnswer> answers = candidateExamAnswerRepository.findByCandidateExamSessionId(sessionId);

        // Transform the list of answers into a list of maps containing questionId and optionId
        return answers.stream()
                .map(answer -> {
                    Map<String, String> result = new HashMap<>();
                    result.put("questionId", String.valueOf(answer.getQuestion().getId()));
                    CandidateExamAnswer candidateAnswer = candidateExamAnswerRepository
                            .findByCandidateExamSessionIdAndQuestionId(sessionId, answer.getQuestion().getId());

                    MCQOption optionId = null;
                    if(candidateAnswer != null) {
                        optionId = candidateExamAnswerRepository.findMcqAnswerTextById(candidateAnswer.getId());
                    }
                    if(optionId != null) {
                        result.put("optionId", String.valueOf(optionId.getId()));
                    }else{
                        result.put("optionId", null);
                    }

                    return result;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public String  setExamCandidateGrade(ExamCandidateGradeRequest examCandidateGradeRequest){
        ExamCandidateGrade examCandidateGrade = new ExamCandidateGrade();
        examCandidateGrade.setExamID(examCandidateGradeRequest.getExamID());
        examCandidateGrade.setCandidateID(examCandidateGradeRequest.getCandidateID());
        examCandidateGrade.setStatus(examCandidateGradeRequest.getStatus());
        examCandidateGrade.setGrade(examCandidateGradeRequest.getGrade());
        examCandidateGrade.setScore(examCandidateGradeRequest.getScore());
        examCandidateGradeRepository.save(examCandidateGrade);
        return "Grade set successfully";
    }

    @Override
    @Transactional
    public List<ExamCandidateGradeResponse> getExamCandidateGrade() {
//        initialize the response list
        List<ExamCandidateGradeResponse> examCandidateGradeResponses = new ArrayList<>();
        List<ExamCandidateGrade> examCandidateGrades = examCandidateGradeRepository.findAll();
        if (examCandidateGrades == null || examCandidateGrades.isEmpty()) {
            throw new IllegalArgumentException("No exam candidate grades found");
        }

        examCandidateGrades.forEach(examCandidateGrade -> {
            ExamCandidateGradeResponse response = new ExamCandidateGradeResponse();
            Exam exam = examRepository.findById(Long.parseLong(examCandidateGrade.getExamID().toString())).orElseThrow(() -> new IllegalArgumentException("Exam not found"));
            Candidate candidate = candidateRepository.findById(Long.parseLong(examCandidateGrade.getCandidateID().toString())).orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
            response.setExamID(String.valueOf(exam.getId()));
            response.setCandidateID(String.valueOf(candidate.getId()));
            response.setExamTitle(exam.getTitle());
            response.setCandidateName(candidate.getFirstName() + " " + candidate.getLastName());
            response.setStatus(examCandidateGrade.getStatus());
            response.setGrade(examCandidateGrade.getGrade());
            response.setScore(examCandidateGrade.getScore());

            examCandidateGradeResponses.add(response);
            });

        return examCandidateGradeResponses;
    }
}
