package com.testify.Testify_Backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testify.Testify_Backend.enums.QuestionType;
import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.ExamRepository;
import com.testify.Testify_Backend.repository.ExamSetterRepository;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.repository.QuestionRepository;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExamManagementServiceImpl implements ExamManagementService {
    private final ExamRepository examRepository;
    private final ExamSetterRepository examSetterRepository;
    private final OrganizationRepository organizationRepository;
    private final QuestionRepository questionRepository;

    private final ObjectMapper objectMapper;

    //Create Exam
    @Override
    public void createExam(ExamRequest examRequest){

        Exam exam = Exam.builder()
                .title(examRequest.getTitle())
                .examSetter(examSetterRepository.findById(examRequest.getExamSetterId()).get())
                .organization(organizationRepository.findById(examRequest.getOrganizationId()).get())
                .description(examRequest.getDescription())
                .instructions(examRequest.getInstructions())
                .duration(examRequest.getDuration())
                .totalMarks(examRequest.getTotalMarks())
                .passMarks(examRequest.getPassMarks())
                .startDatetime(examRequest.getStartDatetime())
                .endDatetime(examRequest.getEndDatetime())
                .isPrivate(examRequest.isPrivate())
                .build();
        examRepository.save(exam);
    }





    //Get exam
    public Exam getExam(long examId){
        return examRepository.findById(examId).get();
    }

    public GenericAddOrUpdateResponse<QuestionRequest> addQuestion(long examId, QuestionRequest questionRequest) {
        GenericAddOrUpdateResponse<QuestionRequest> response = new GenericAddOrUpdateResponse<>();
        Optional<Exam> exam = examRepository.findById(examId);

        try {
            String questionRequestJson = objectMapper.writeValueAsString(questionRequest);
            log.info("Received question request: {}", questionRequestJson);
        } catch (JsonProcessingException e) {
            log.error("Failed to convert question request to JSON", e);
        }

        log.info("Exam id: "+examId);
        //make question object
        if (exam.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Exam not found");
            return response;
        }

        Question question=null;
        log.info("Question type: "+questionRequest.getQuestionText());

        if (questionRequest.getType() == QuestionType.MCQ) {
            log.info("MCQ eka athuleeeeee");
            MCQRequest mcqRequest = (MCQRequest) questionRequest; // Cast to MCQRequest
            MCQ mcq = MCQ.builder()
                    .questionText(mcqRequest.getQuestionText())
                    .exam(exam.get())
                    .build();

            Set<MCQOption> options = mcqRequest.getOptions().stream().map(optionRequest -> {
                MCQOption option = MCQOption.builder()
                        .optionText(optionRequest.getOptionText())
                        .isCorrect(optionRequest.isCorrect())
                        .marks(optionRequest.getMarks()) // Set the marks
                        .build();
                option.setMcqQuestion(mcq); // Set the relationship
                return option;
            }).collect(Collectors.toSet());

            mcq.setOptions(options); // Set options to mcq
            question = mcq;
        }else if(questionRequest.getType().equals(QuestionType.ESSAY)){
            EssayRequest essayRequest = (EssayRequest) questionRequest;
            Essay essay = Essay.builder()
                    .questionText(essayRequest.getQuestionText())
                    .exam(exam.get())
                    .build();
            Set<EssayCoverPoint> coverPoints = essayRequest.getCoveringPoints().stream().map(coverPointRequest -> {
                EssayCoverPoint coverPoint = EssayCoverPoint.builder()
                        .coverPointText(coverPointRequest.getCoverPointText())
                        .marks(coverPointRequest.getMarks()) // Set the marks
                        .build();
                coverPoint.setEssayQuestion(essay); // Set the relationship
                return coverPoint;
            }).collect(Collectors.toSet());

            essay.setCoverPoints(coverPoints); // Set cover points to essay
            question = essay;
        }

        Question savedQuestion=questionRepository.save(question); // Save the question

        response.setSuccess(true);
        response.setMessage("Question added successfully");
        response.setId(savedQuestion.getId());
        return response;


    }
}
