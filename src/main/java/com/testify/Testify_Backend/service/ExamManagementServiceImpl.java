package com.testify.Testify_Backend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.exam_management.QuestionSequenceResponse;
import com.testify.Testify_Backend.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    private final MCQQuestionRepository mcqQuestionRepository;
    private final EssayQuestionRepository essayQuestionRepository;
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

    public GenericAddOrUpdateResponse<MCQRequest> saveMCQ(MCQRequest mcqRequest){
        GenericAddOrUpdateResponse<MCQRequest> response = new GenericAddOrUpdateResponse<>();
        Optional<Exam> exam = examRepository.findById(mcqRequest.getExamId());

<<<<<<< Updated upstream
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
=======
>>>>>>> Stashed changes
        if (exam.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Exam not found");
            return response;
        }

        MCQ mcq = MCQ.builder()
                .questionText(mcqRequest.getQuestionText())
                .exam(exam.get())
                .isDeleted(false)
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
        mcqQuestionRepository.save(mcq); // Save the question

        response.setSuccess(true);
        response.setId(mcq.getId());
        response.setMessage("MCQ added successfully");
        return response;
    }

    @Transactional
    public GenericAddOrUpdateResponse<MCQUpdateRequest> updateMCQQuestion(MCQUpdateRequest mcqUpdateRequest) {
        GenericAddOrUpdateResponse<MCQUpdateRequest> response = new GenericAddOrUpdateResponse<>();

        try {
            // Fetch MCQ by ID, throw exception if not found
            MCQ mcq = mcqQuestionRepository.findById(mcqUpdateRequest.getId())
                    .orElseThrow(() -> new IllegalArgumentException("MCQ not found with id: " + mcqUpdateRequest.getId()));

            // Update question text
            mcq.setQuestionText(mcqUpdateRequest.getQuestionText());

            // Get the current options
            Set<MCQOption> currentOptions = mcq.getOptions();
            currentOptions.clear();  // Clear the options, which will trigger orphan removal

            // Map new options from request and add them to the existing set (without replacing the reference)
            mcqUpdateRequest.getOptions().forEach(optionDTO -> {
                MCQOption option = new MCQOption();
                option.setOptionText(optionDTO.getOptionText());
                option.setCorrect(optionDTO.isCorrect());
                option.setMarks(optionDTO.getMarks());
                option.setMcqQuestion(mcq); // Set the relationship back to MCQ
                currentOptions.add(option); // Add each option to the existing set
            });

            // Save updated MCQ
            mcqQuestionRepository.save(mcq);

            // Set response as success
            response.setSuccess(true);
            response.setMessage("MCQ updated successfully");
            response.setId(mcq.getId());

        } catch (IllegalArgumentException e) {
            // Handle case where MCQ is not found
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions
            response.setSuccess(false);
            response.setMessage("An error occurred while updating the MCQ: " + e.getMessage());
        }

        return response;
    }





    public GenericAddOrUpdateResponse<EssayRequest> saveEssay(EssayRequest essayRequest){
        GenericAddOrUpdateResponse<EssayRequest> response = new GenericAddOrUpdateResponse<>();
        Optional<Exam> exam = examRepository.findById(essayRequest.getExamId());

        if (exam.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Exam not found");
            return response;
        }

        Essay essay = Essay.builder()
                .questionText(essayRequest.getQuestionText())
                .exam(exam.get())
                .isDeleted(false)
                .build();

        Set<EssayCoverPoint> coverPoints = essayRequest.getCoveringPoints().stream().map(coverPointRequest -> {
            EssayCoverPoint coverPoint = EssayCoverPoint.builder()
                    .coverPointText(coverPointRequest.getCoverPointText())
                    .marks(coverPointRequest.getMarks())
                    .build();
            coverPoint.setEssayQuestion(essay);
            return coverPoint;
        }).collect(Collectors.toSet());

        essay.setCoverPoints(coverPoints);
        essayQuestionRepository.save(essay);

        response.setSuccess(true);
        response.setId(essay.getId());
        response.setMessage("Essay added successfully");
        return response;
    }

    @Transactional
    public GenericAddOrUpdateResponse<EssayUpdateRequest> updateEssayQuestion(EssayUpdateRequest essayUpdateRequest) {
        GenericAddOrUpdateResponse<EssayUpdateRequest> response = new GenericAddOrUpdateResponse<>();

        try {
            // Fetch the Essay Question by ID, throw exception if not found
            Essay essay = essayQuestionRepository.findById(essayUpdateRequest.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Essay question not found with id: " + essayUpdateRequest.getId()));

            // Update the question text
            essay.setQuestionText(essayUpdateRequest.getQuestionText());

            // Clear the current cover points (this removes the old ones)
            Set<EssayCoverPoint> currentCoverPoints = essay.getCoverPoints();
            currentCoverPoints.clear();

            // Create new cover points from the update request
            Set<EssayCoverPoint> updatedCoveringPoints = essayUpdateRequest.getCoveringPoints().stream().map(coverPointRequest -> {
                EssayCoverPoint coverPoint = new EssayCoverPoint();
                coverPoint.setCoverPointText(coverPointRequest.getCoverPointText());
                coverPoint.setMarks(coverPointRequest.getMarks());
                coverPoint.setEssayQuestion(essay); // Ensure bi-directional reference
                return coverPoint;
            }).collect(Collectors.toSet());

            // Add the new cover points
            currentCoverPoints.addAll(updatedCoveringPoints);

            // Save the updated Essay question
            essayQuestionRepository.save(essay);

            // Set response as success
            response.setSuccess(true);
            response.setMessage("Essay question updated successfully");
            response.setId(essay.getId());

        } catch (IllegalArgumentException e) {
            // Handle case where Essay question is not found
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions
            response.setSuccess(false);
            response.setMessage("An error occurred while updating the Essay question: " + e.getMessage());
        }

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

    @Transactional(readOnly = true)
    public ResponseEntity<QuestionSequenceResponse> getQuestionSequence(long examId) {
        QuestionSequenceResponse response = new QuestionSequenceResponse();

        try {
            // Find the exam by ID or throw an exception if not found
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + examId));

            // Retrieve question sequence
            List<Long> questionIds = exam.getQuestionSequence();

            // Set response with the retrieved sequence
            response.setExamId(examId);
            response.setQuestionIds(questionIds);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Log the error and set the error message in the response
            log.error("Error retrieving question sequence for examId {}: {}", examId, e.getMessage());

            response.setExamId(examId);
            response.setErrorMessage("Exam not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // Send 404 status with error message
        } catch (Exception e) {
            // Log the unexpected error and set the error message in the response
            log.error("Unexpected error retrieving question sequence for examId {}: {}", examId, e.getMessage(), e);

            response.setExamId(examId);
            response.setErrorMessage("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // Send 500 status with error message
        }
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
