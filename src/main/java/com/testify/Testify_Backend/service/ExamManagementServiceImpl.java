package com.testify.Testify_Backend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testify.Testify_Backend.enums.QuestionType;
import com.testify.Testify_Backend.enums.UserRole;
import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    private final CandidateRepository candidateRepository;
    private  final UserRepository userRepository;

    private final ObjectMapper objectMapper;

    //Create Exam
    @Override
    public GenericAddOrUpdateResponse<ExamRequest> createExam(ExamRequest examRequest) {
        GenericAddOrUpdateResponse<ExamRequest> response = new GenericAddOrUpdateResponse<>();

        String currentUserEmail = UserUtil.getCurrentUserName();
        log.info("Current User Email: {}", currentUserEmail);

        Optional<User> optionalUser = userRepository.findByEmail(currentUserEmail);

        Organization organization = organizationRepository.findById(examRequest.getOrganizationId())
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        if(optionalUser.isEmpty()){
            response.setSuccess(false);
            response.setMessage("User not found");
            return response;
        }else{
            Exam exam = Exam.builder()
                    .title(examRequest.getTitle())
                    .createdBy(optionalUser.get())
                    .organization(organization)
                    .description(examRequest.getDescription())
                    .instructions(examRequest.getInstructions())
                    .duration(examRequest.getDuration())
                    .startDatetime(examRequest.getStartDatetime())
                    .endDatetime(examRequest.getEndDatetime())
                    .isPrivate(true)
                    .build();
            exam = examRepository.save(exam);

            response.setSuccess(true);
            response.setMessage("Exam created successfully");
            response.setId(exam.getId());
            return response;
        }




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

    @Override
    @Transactional
    public GenericAddOrUpdateResponse<QuestionSequenceRequest> updateQuestionSequence(long examId, QuestionSequenceRequest questionSequenceRequest) {
        GenericAddOrUpdateResponse<QuestionSequenceRequest> response = new GenericAddOrUpdateResponse<>();
        try {
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + examId));
            exam.setQuestionSequence(questionSequenceRequest.getQuestionIds());
            examRepository.save(exam);

            response.setSuccess(true);
            response.setMessage("Question sequence updated successfully");
            response.setId(examId);
        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage("Error updating question sequence: " + e.getMessage());
            response.setId(examId);
            log.error("Error updating question sequence for examId {}: {}", examId, e.getMessage());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Unexpected error updating question sequence");
            response.setId(examId);
            log.error("Unexpected error updating question sequence for examId {}: {}", examId, e.getMessage(), e);
        }

        return response;
    }

    public ResponseEntity<Exam> getExamResponse(long examId){
        Exam exam = examRepository.findById(examId).orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + examId));
        return ResponseEntity.ok(exam);
    }

    @Override
    @Transactional
    public GenericAddOrUpdateResponse<CandidateEmailListRequest> addCandidatesToExam(long examId, CandidateEmailListRequest request) {
        GenericAddOrUpdateResponse<CandidateEmailListRequest> response = new GenericAddOrUpdateResponse<>();

        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

        List<Candidate> candidates = request.getEmails().stream()
                .map(email -> candidateRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("Candidate not found with email: " + email)))
                .collect(Collectors.toList());

        exam.getCandidates().addAll(candidates);
        examRepository.save(exam);

        response.setSuccess(true);
        response.setMessage("candidate added successfully");
        return response;
    }

}
