package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.CandidateExamAnswerRepository;
import com.testify.Testify_Backend.repository.ExamSessionRepository;
import com.testify.Testify_Backend.repository.GradeRepository;
import com.testify.Testify_Backend.repository.QuestionRepository;
import com.testify.Testify_Backend.responses.EssayDetailsResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GradingServiceImpl implements GradingService {

    private final QuestionRepository questionRepository;
    private final CandidateExamAnswerRepository candidateExamAnswerRepository;
    private final GradeRepository gradeRepository;
    private final ExamSessionRepository examSessionRepository;

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
}
