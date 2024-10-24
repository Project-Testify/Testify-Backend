package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.enums.OrderType;
import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.exam_management.*;
import com.testify.Testify_Backend.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.LogManager;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
    private final RandomOrderRepository randomOrderRepository;
    private final FixedOrderRepository fixedOrderRepository;

    private final ModelMapper modelMapper;



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
                    .orderType(examRequest.getOrderType() != null ? examRequest.getOrderType() : OrderType.FIXED)
                    .build();
            exam = examRepository.save(exam);

            if (exam.getOrderType() == OrderType.FIXED) {
                FixedOrder fixedOrder = new FixedOrder();
                fixedOrder.setExam(exam);
                fixedOrder.setFixedOrderValue(null);  // Save as null
                fixedOrderRepository.save(fixedOrder);
                exam.setFixedOrder(fixedOrder);
            } else if (exam.getOrderType() == OrderType.RANDOM) {
                RandomOrder randomOrder = new RandomOrder();
                randomOrder.setExam(exam);
                randomOrder.setRandomOrderValue(null);  // Save as null
                randomOrderRepository.save(randomOrder);
                exam.setRandomOrder(randomOrder);
            }

            response.setSuccess(true);
            response.setMessage("Exam created successfully");
            response.setId(exam.getId());
            return response;
        }

    }

    @Override
    public GenericAddOrUpdateResponse<ExamUpdateRequest> updateExam(long examId, ExamUpdateRequest examUpdateRequest) {
        GenericAddOrUpdateResponse<ExamUpdateRequest> response = new GenericAddOrUpdateResponse<>();

        // Find the existing exam by ID
        Exam existingExam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + examId));

        // Update the exam details
        existingExam.setTitle(examUpdateRequest.getTitle());
        existingExam.setDescription(examUpdateRequest.getDescription());
        existingExam.setInstructions(examUpdateRequest.getInstructions());
        existingExam.setDuration(examUpdateRequest.getDuration());
        existingExam.setStartDatetime(examUpdateRequest.getStartDatetime());
        existingExam.setEndDatetime(examUpdateRequest.getEndDatetime());
        existingExam.setPrivate(examUpdateRequest.isPrivate());

        // If the organization ID is provided, update the organization
        if (examUpdateRequest.getOrganizationId() != null) {
            Organization organization = organizationRepository.findById(examUpdateRequest.getOrganizationId())
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found"));
            existingExam.setOrganization(organization);
        }

        // Save the updated exam
        examRepository.save(existingExam);

        response.setSuccess(true);
        response.setMessage("Exam updated successfully");
        response.setId(existingExam.getId());
        return response;
    }


    public GenericAddOrUpdateResponse<MCQRequest> saveMCQ(MCQRequest mcqRequest){
        GenericAddOrUpdateResponse<MCQRequest> response = new GenericAddOrUpdateResponse<>();
        Optional<Exam> exam = examRepository.findById(mcqRequest.getExamId());

        if (exam.isEmpty()) {
            response.setSuccess(false);
            response.setMessage("Exam not found");
            return response;
        }

        MCQ mcq = MCQ.builder()
                .questionText(mcqRequest.getQuestionText())
                .exam(exam.get())
                .difficultyLevel(mcqRequest.getDifficultyLevel().toUpperCase())
                .isDeleted(false)
                .build();

        Set<MCQOption> options = mcqRequest.getOptions().stream().map(optionRequest -> {
            MCQOption option = MCQOption.builder()
                    .optionText(optionRequest.getOptionText())
                    .correct(optionRequest.isCorrect()) // Convert string to boolean
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
            mcq.setDifficultyLevel(mcqUpdateRequest.getDifficultyLevel().toUpperCase());

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
                .difficultyLevel(essayRequest.getDifficultyLevel().toUpperCase())
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
            essay.setDifficultyLevel(essayUpdateRequest.getDifficultyLevel().toUpperCase());

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

    @Transactional
    public ResponseEntity<GenericDeleteResponse<Void>> deleteQuestion(long questionId) {
        GenericDeleteResponse<Void> response = new GenericDeleteResponse<>();

        try {
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));

            question.setDeleted(true);
            questionRepository.save(question);

            response.setSuccess(true);
            response.setMessage("Question deleted successfully");
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage("Error deleting question: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            log.error("Unexpected error deleting question with id {}: {}", questionId, e.getMessage(), e);
            response.setSuccess(false);
            response.setMessage("Unexpected error deleting question");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
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

    @Transactional(readOnly = true)
    public ResponseEntity<QuestionListResponse> getAllQuestionsByExamId(long examId) {
        QuestionListResponse response = new QuestionListResponse();
        List<QuestionResponse> questionResponses = new ArrayList<>();

        try {
            // Fetch questions associated with the exam ID that are not deleted
            List<Question> questions = questionRepository.findAllActiveQuestionsByExamId(examId);

            // Check if questions are found
            if (questions.isEmpty()) {
                // Return null instead of an error message
                return ResponseEntity.ok(null); // Return 200 OK with null body
            }

            for (Question question : questions) {
                String questionType = question instanceof MCQ ? "MCQ" : "Essay";

                List<MCQOptionResponse> options = null;
                List<EssayCoverPointResponse> coverPoints = null;

                // Populate options for MCQs
                if (question instanceof MCQ) {
                    options = ((MCQ) question).getOptions().stream()
                            .map(option -> MCQOptionResponse.builder()
                                    .optionId(option.getId())
                                    .optionText(option.getOptionText())
                                    .correct(option.isCorrect())
                                    .marks(option.getMarks())
                                    .build())
                            .collect(Collectors.toList());
                }
                // Populate cover points for essays
                else if (question instanceof Essay) {
                    coverPoints = ((Essay) question).getCoverPoints().stream()
                            .map(point -> EssayCoverPointResponse.builder()
                                    .coverPointId(point.getId())
                                    .coverPointText(point.getCoverPointText())
                                    .marks(point.getMarks())
                                    .build())
                            .collect(Collectors.toList());
                }

                // Build the QuestionResponse object
                QuestionResponse questionResponse = QuestionResponse.builder()
                        .questionId(question.getId())
                        .questionText(question.getQuestionText())
                        .questionType(questionType)
                        .options(options)
                        .coverPoints(coverPoints)
                        .build();

                questionResponses.add(questionResponse);
            }

            // Set the response with the questions
            response.setExamId(examId);
            response.setQuestions(questionResponses);
            return ResponseEntity.ok(response);  // Return 200 OK with questions

        } catch (IllegalArgumentException e) {
            // Handle specific exceptions
            response.setErrorMessage("Invalid request: " + e.getMessage());
            return ResponseEntity.badRequest().body(response); // Return 400 Bad Request
        } catch (Exception e) {
            // Log unexpected errors and return a server error response
            log.error("Unexpected error retrieving questions for exam ID {}: {}", examId, e.getMessage(), e);
            response.setErrorMessage("Unexpected error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);  // Return 500 Internal Server Error
        }
    }





    @Transactional(readOnly = true)
    public ResponseEntity<QuestionResponse> getQuestionById(long questionId) {
        try {
            // Find the question by ID
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + questionId));

            // Build the response based on the question type
            QuestionResponse.QuestionResponseBuilder responseBuilder = QuestionResponse.builder()
                    .questionId(question.getId())
                    .questionText(question.getQuestionText());

            // Determine the question type
            String questionType = question instanceof MCQ ? "MCQ" : "Essay";
            responseBuilder.questionType(questionType);

            if (question instanceof MCQ) {
                // Map MCQ options manually
                List<MCQOptionResponse> options = ((MCQ) question).getOptions().stream()
                        .map(option -> MCQOptionResponse.builder()
                                .optionId(option.getId())
                                .optionText(option.getOptionText())
                                .correct(option.isCorrect())
                                .marks(option.getMarks())
                                .build())
                        .collect(Collectors.toList());
                responseBuilder.options(options);
            } else if (question instanceof Essay) {
                // Map Essay cover points manually
                List<EssayCoverPointResponse> coverPoints = ((Essay) question).getCoverPoints().stream()
                        .map(point -> EssayCoverPointResponse.builder()
                                .coverPointId(point.getId())
                                .coverPointText(point.getCoverPointText())
                                .marks(point.getMarks())
                                .build())
                        .collect(Collectors.toList());
                responseBuilder.coverPoints(coverPoints);
            }

            QuestionResponse response = responseBuilder.build(); // Build the response

            return ResponseEntity.ok(response); // Return 200 OK with the question data

        } catch (IllegalArgumentException e) {
            // Log the error and return NOT FOUND response
            log.error("Error retrieving question with id {}: {}", questionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Return 404 NOT FOUND
        } catch (Exception e) {
            // Log the unexpected error
            log.error("Unexpected error retrieving question with id {}: {}", questionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null); // Return 500 INTERNAL SERVER ERROR
        }
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

    public ExamResponse getExamById(long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + examId));

        // Build the ExamResponse object
        ExamResponse examResponse = ExamResponse.builder()
                .id(exam.getId())
                .title(exam.getTitle())
                .description(exam.getDescription())
                .instructions(exam.getInstructions())
                .duration(exam.getDuration())
                .startDatetime(exam.getStartDatetime())
                .endDatetime(exam.getEndDatetime())
                .isPrivate(exam.isPrivate())

                // Handle null check for createdBy
                .createdBy(UserResponse.builder()
                        .id(exam.getCreatedBy().getId())
                        .email(exam.getCreatedBy().getEmail())
                        .build())

                // Handle null check for organization
                .organization(OrganizationResponse.builder()
                        .id(exam.getOrganization().getId())
                        .firstName(exam.getOrganization().getFirstName())
                        .build())

                // Handle null check for moderator
                .moderator(Optional.ofNullable(exam.getModerator())
                        .map(moderator -> ExamSetterResponse.builder()
                                .id(moderator.getId())
                                .email(moderator.getEmail())
                                .firstName(moderator.getFirstName())
                                .lastName(moderator.getLastName())
                                .build())
                        .orElse(null))

                // Handle null check for proctors
                .proctors(Optional.ofNullable(exam.getProctors())
                        .map(proctors -> proctors.stream()
                                .map(proctor -> ExamSetterResponse.builder()
                                        .id(proctor.getId())
                                        .email(proctor.getEmail())
                                        .firstName(proctor.getFirstName())
                                        .lastName(proctor.getLastName())
                                        .build())
                                .collect(Collectors.toSet()))
                        .orElse(Collections.emptySet())) // Return an empty set if proctors is null

                // Handle null check for candidates
                .candidates(Optional.ofNullable(exam.getCandidates())
                        .map(candidates -> candidates.stream()
                                .map(candidate -> CandidateResponse.builder()
                                        .id(candidate.getId())
                                        .email(candidate.getEmail())
                                        .firstName(candidate.getFirstName())
                                        .lastName(candidate.getLastName())
                                        .build())
                                .collect(Collectors.toSet()))
                        .orElse(Collections.emptySet())) // Return an empty set if candidates is null

                .orderType(exam.getOrderType())

                .fixedOrder(Optional.ofNullable(exam.getFixedOrder())
                        .map(fixedOrder -> FixedOrderResponse.builder()
                                .fixedOrderValue(fixedOrder.getFixedOrderValue())
                                .build())
                        .orElse(null))

                .randomOrder(Optional.ofNullable(exam.getRandomOrder())
                        .map(randomOrder -> RandomOrderResponse.builder()
                                .randomOrderValue(randomOrder.getRandomOrderValue())
                                .build())
                        .orElse(null))

                // Handle question sequence (assuming it's never null)
                .questionSequence(exam.getQuestionSequence())

                .build();

        return examResponse;
    }

    @Transactional
    @Override
    public GenericAddOrUpdateResponse<OrderChangeRequest> updateOrder(long examId, OrderChangeRequest orderRequest) {

        GenericAddOrUpdateResponse<OrderChangeRequest> response = new GenericAddOrUpdateResponse<>();

        // Find the exam by id
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new IllegalArgumentException("Exam not found with id: " + examId));

        exam.setOrderType(orderRequest.getOrderType());
        examRepository.save(exam);

        // Determine whether to update FixedOrder or RandomOrder
        if (orderRequest.getOrderType() == OrderType.FIXED) {
            // Check if a fixed order exists for this exam
            FixedOrder fixedOrder = fixedOrderRepository.findByExamId(examId)
                    .orElseGet(() -> FixedOrder.builder()
                            .exam(exam)
                            .fixedOrderValue(null)  // Setting initial value as null
                            .build());

            // Update or create the fixed order value
            fixedOrder.setFixedOrderValue(orderRequest.getValue());
            fixedOrder = fixedOrderRepository.save(fixedOrder);

            response.setSuccess(true);
            response.setMessage("Fixed order " + (fixedOrder.getId() == null ? "added" : "updated") + " successfully.");
            response.setId(fixedOrder.getId());

        } else if (orderRequest.getOrderType() == OrderType.RANDOM) {
            // Check if a random order exists for this exam
            RandomOrder randomOrder = randomOrderRepository.findByExamId(examId)
                    .orElseGet(() -> RandomOrder.builder()
                            .exam(exam)
                            .randomOrderValue(null)  // Setting initial value as null
                            .build());

            // Update or create the random order value
            randomOrder.setRandomOrderValue(orderRequest.getValue());
            randomOrder = randomOrderRepository.save(randomOrder);

            response.setSuccess(true);
            response.setMessage("Random order " + (randomOrder.getId() == null ? "added" : "updated") + " successfully.");
            response.setId(randomOrder.getId());


        } else {
            response.setSuccess(false);
            response.setMessage("Invalid order type: " + orderRequest.getOrderType());
            throw new IllegalArgumentException("Invalid order type: " + orderRequest.getOrderType());

        }

        // Return a generic response
        return response;
    }


}
