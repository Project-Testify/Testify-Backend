package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.enums.ExamType;
import com.testify.Testify_Backend.enums.OrderType;
import com.testify.Testify_Backend.enums.QuestionType;
import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.requests.exam_management.*;
import com.testify.Testify_Backend.responses.CandidateGroupResponse;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.exam_management.*;
import com.testify.Testify_Backend.utils.UserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.LogManager;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final GradeRepository gradeRepository;
    private final ExamSessionRepository examSessionRepository;
    private final CandidateExamAnswerRepository candidateExamAnswerRepository;
    private final MCQOptionRepository mcqOptionRepository;
    private final CandidateGroupRepository candidateGroupRepository;
    private final CandidateGroupRepository candidateGroupRepository;
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
        try{
            Optional<Exam> exam = examRepository.findById(mcqRequest.getExamId());

            if (exam.isEmpty()) {
                response.setSuccess(false);
                response.setMessage("Exam not found");
                return response;
            }

            MCQ mcq = MCQ.builder()
                    .questionText(mcqRequest.getQuestionText())
                    .exam(exam.get())
                    .type(QuestionType.MCQ)
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

        } catch (Exception e) {
            // Handle case where MCQ is not found
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
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
                .type(QuestionType.ESSAY)
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

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<QuestionListResponse> getAllQuestionsByExamId(long examId) {
        QuestionListResponse response = new QuestionListResponse();
        List<QuestionResponse> questionResponses = new ArrayList<>();

        try {
            // Extract username from JWT
            String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

            if (currentUserEmail == null) {
                throw new IllegalStateException("No authenticated user found");
            }

            // Fetch the role of the user
            String role = userRepository.findByEmail(currentUserEmail)
                    .map(user -> user.getRole().name()) // Convert UserRole enum to String
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // Fetch questions associated with the exam ID that are not deleted
            List<Question> questions = questionRepository.findAllActiveQuestionsByExamId(examId);
            Optional<Exam> examOptional = examRepository.findById(examId);

            if (examOptional.isEmpty()) {
                throw new IllegalArgumentException("Exam not found for ID: " + examId);
            }

            System.out.println(role);

            Exam exam = examOptional.get();
            ExamType examType = exam.getExamType();

            // Check if questions are found
            if (questions.isEmpty()) {
                // Return null instead of an error message
                return ResponseEntity.ok(null);
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
                                    // Exclude 'correct' flag if the user is a candidate
                                    .correct(!role.equals("CANDIDATE") && option.isCorrect())
                                    .marks(role.equals("CANDIDATE") ? 0.00 : option.getMarks())
                                    .build())
                            .collect(Collectors.toList());
                }

                // Populate cover points for essays
                else if (question instanceof Essay) {
                    // Exclude cover points if the user is a candidate
                    if (!role.equals("CANDIDATE")) {
                        coverPoints = ((Essay) question).getCoverPoints().stream()
                                .map(point -> EssayCoverPointResponse.builder()
                                        .coverPointId(point.getId())
                                        .coverPointText(point.getCoverPointText())
                                        .marks(point.getMarks())
                                        .build())
                                .collect(Collectors.toList());
                    }
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
            response.setExamType(examType);
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

    @Transactional
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

    @Override
    public ResponseEntity<OrderResponse> getExamOrderTypeAndValue(Long examId) {
        try {
            Optional<Exam> examOptional = examRepository.findById(examId);
            if (examOptional.isPresent()) {
                Exam exam = examOptional.get();
                OrderType orderType = exam.getOrderType();
                Integer value = null;

                switch (orderType) {
                    case FIXED:
                        FixedOrder fixedOrder = exam.getFixedOrder();
                        if (fixedOrder != null) {
                            value = fixedOrder.getFixedOrderValue();
                        }
                        break;
                    case RANDOM:
                        RandomOrder randomOrder = exam.getRandomOrder();
                        if (randomOrder != null) {
                            value = randomOrder.getRandomOrderValue();
                        }
                        break;
                    default:
                        // Handle other potential order types, if necessary
                        break;
                }

                OrderResponse orderResponse = new OrderResponse(orderType, value);
                return ResponseEntity.ok(orderResponse);
            } else {
                // Return an empty response with status OK if no exam found
                return ResponseEntity.ok(new OrderResponse());
            }
        } catch (Exception e) {
            // Log the error and return a generic response to avoid exposing details
            e.printStackTrace(); // Consider using a proper logging framework
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new OrderResponse());
        }
    }



    public ResponseEntity<GenericAddOrUpdateResponse> saveGrades(Long examId, List<GradeRequest> gradeRequestList) {
        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();

        try {
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new RuntimeException("Exam not found"));

            List<Grade> grades = gradeRequestList.stream()
                    .map(dto -> new Grade(0, dto.getGradingString(), dto.getMinMarks(), dto.getMaxMarks(), exam))
                    .collect(Collectors.toList());

            gradeRepository.saveAll(grades);

            response.setSuccess(true);
            response.setMessage("Grades added successfully");

        } catch (RuntimeException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());

        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage("An unexpected error occurred while saving grades");
        }

        return ResponseEntity.ok(response);
    }

    @Transactional(readOnly = true)
    public ResponseEntity<List<GradeResponse>> getGradesByExamId(Long examId) {
        List<GradeResponse> gradeResponses;

        try {
            List<Grade> grades = gradeRepository.findByExamId(examId);

            log.info("Grades: {}", grades);

            if (grades.isEmpty()) {
                throw new RuntimeException("No grades found for the given exam ID");
            }

            gradeResponses = grades.stream()
                    .map(grade -> new GradeResponse(grade.getId(), grade.getGrade(), grade.getMinMarks(), grade.getMaxMarks()))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(gradeResponses);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Transactional
    public ResponseEntity<GenericAddOrUpdateResponse> updateGrades(Long examId, List<GradeRequest> gradeRequestList) {
        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();

        try {
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new RuntimeException("Exam not found"));

            // Delete existing grades for the exam
            gradeRepository.deleteByExamId(examId);

            // Map and save new grades
            List<Grade> newGrades = gradeRequestList.stream()
                    .map(dto -> new Grade(0, dto.getGradingString(), dto.getMinMarks(), dto.getMaxMarks(), exam))
                    .collect(Collectors.toList());

            gradeRepository.saveAll(newGrades);

            response.setSuccess(true);
            response.setMessage("Grades updated successfully");
        } catch (RuntimeException ex) {
            response.setSuccess(false);
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            response.setSuccess(false);
            response.setMessage("An unexpected error occurred while updating grades");
        }

        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ExamSessionResponse startExam(StartExamRequest request) {
        // Get the username (email) from SecurityContextHolder
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (currentUserEmail == null) {
            throw new IllegalStateException("No authenticated user found");
        }

        Candidate candidate = candidateRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found for email: " + currentUserEmail));

        // Validate Exam
        Exam exam = examRepository.findById(request.getExamId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid exam ID"));

        // Get all questions of the exam and shuffle them to randomize order
        List<Question> questions = questionRepository.findByExamId(exam.getId());
        if (questions.isEmpty()) {
            throw new IllegalArgumentException("No questions found for this exam");
        }

        // Shuffle questions to randomize the order
        Collections.shuffle(questions);

        // Set the start time and end time (end time should be calculated based on exam duration)
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusMinutes(exam.getDuration()); // Assuming exam has a duration in minutes

        // Create and save the exam session
        CandidateExamSession session = new CandidateExamSession();
        session.setCandidate(candidate);
        session.setExam(exam);
        session.setStartTime(startTime);
        session.setEndTime(endTime);
        session.setInProgress(true);
        session.setCurrentQuestionIndex(0); // Start from the first question
        session.setAnswers(new ArrayList<>()); // Empty answers at the start

        // Save session
        CandidateExamSession savedSession = examSessionRepository.save(session);

        // Map to response DTO
        ExamSessionResponse response = modelMapper.map(savedSession, ExamSessionResponse.class);

        return response;
    }

    @Override
    public void saveAnswer(Long sessionId, Long questionId, Long optionId, String answerText) {
        // Find the candidate exam session
        CandidateExamSession session = examSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid session ID"));

        // Find the question
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));

        // Check if the answerText is an empty string and convert it to null if necessary
        if (answerText != null && answerText.trim().isEmpty()) {
            answerText = null;
        }

        // Handle answer saving based on question type
        if (question.getType().equals(QuestionType.MCQ)) {
            // Find the selected option for MCQ
            MCQOption option = mcqOptionRepository.findById(optionId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid option ID"));

            // Create and save the MCQAnswer
            MCQAnswer mcqAnswer = new MCQAnswer();
            mcqAnswer.setCandidateExamSession(session);
            mcqAnswer.setQuestion(question);
            mcqAnswer.setOption(option);  // Option is set for MCQ

            candidateExamAnswerRepository.save(mcqAnswer);
        } else if (question.getType().equals(QuestionType.ESSAY)) {
            // Create and save the EssayAnswer
            EssayAnswer essayAnswer = new EssayAnswer();
            essayAnswer.setCandidateExamSession(session);
            essayAnswer.setQuestion(question);

            // Set essay answer text, if provided
            if (answerText != null) {
                essayAnswer.setAnswerText(answerText);  // Only set if not null
            }

            candidateExamAnswerRepository.save(essayAnswer);
        } else {
            throw new IllegalArgumentException("Unsupported question type");
        }
    }
    public ResponseEntity<GenericAddOrUpdateResponse> addProctorsToExam(long examId, List<String> proctorEmails) {
        Optional<Exam> optionalExam = examRepository.findById(examId);
        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();

        if (!optionalExam.isPresent()) {
            response.setSuccess(false);
            response.setMessage("Exam not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        Exam exam = optionalExam.get();
        log.info("Exam found with ID: {}", examId);

        // If the email list is empty, clear all existing proctors and save
        if (proctorEmails == null || proctorEmails.isEmpty()) {
            log.info("Received empty proctor email list. Removing all existing proctors for exam ID: {}", examId);
            exam.getProctors().clear();
            examRepository.save(exam);

            response.setSuccess(true);
            response.setMessage("All proctors removed successfully");
            return ResponseEntity.ok(response);
        }

        // Clear existing proctors
        Set<ExamSetter> existingProctors = exam.getProctors();
        log.info("Existing proctors: {}", existingProctors);
        existingProctors.clear();

        // Fetch new proctors based on email
        Set<ExamSetter> newProctors = new HashSet<>();
        for (String email : proctorEmails) {
            Optional<ExamSetter> examSetterOpt = examSetterRepository.findByEmail(email);
            if (examSetterOpt.isPresent()) {
                log.info("Proctor found with email: {}", email);
                newProctors.add(examSetterOpt.get());
            } else {
                response.setSuccess(false);
                response.setMessage("Proctor with email " + email + " not found");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        // Add new proctors to the exam
        log.info("New proctors: {}", newProctors);
        exam.setProctors(newProctors);
        examRepository.save(exam);

        response.setSuccess(true);
        response.setMessage("Proctors updated successfully");
        return ResponseEntity.ok(response);
    }


    @Transactional(readOnly = true)
    public ResponseEntity<List<ProctorResponse>> getProctorsByExamId(Long examId) {
        try {
            // Try to fetch the exam
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));

            // Convert the exam setters (proctors) to ProctorResponse
            List<ProctorResponse> proctors = exam.getProctors().stream()
                    .map(proctor -> new ProctorResponse(
                            proctor.getId(),
                            proctor.getFirstName(),
                            proctor.getLastName(),
                            proctor.getEmail()))
                    .collect(Collectors.toList());

            // Return the list of proctors
            return ResponseEntity.ok(proctors);

        } catch (RuntimeException ex) {
            // Return 404 if the exam is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        } catch (Exception ex) {
            // Return 500 if an unexpected error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Transactional
    public ResponseEntity<GenericAddOrUpdateResponse<CandidateEmailListRequest>> updateExamCandidates(
            Long examId, List<String> candidateEmails) {

        GenericAddOrUpdateResponse<CandidateEmailListRequest> response = new GenericAddOrUpdateResponse<>();

        try {
            // Find the exam by ID
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

            // Clear existing candidates from the exam
            exam.getCandidates().clear();

            // Find candidates by email and add them to the exam
            Set<Candidate> newCandidates = candidateEmails.stream()
                    .map(email -> candidateRepository.findByEmail(email)
                            .orElseThrow(() -> new RuntimeException("Candidate not found with email: " + email)))
                    .collect(Collectors.toSet());

            // Add new candidates and save the updated exam
            exam.getCandidates().addAll(newCandidates);
            examRepository.save(exam);

            // Success response
            response.setSuccess(true);
            response.setMessage("Candidates updated successfully.");
            response.setId(examId);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Handle exam or candidate not found exceptions
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            response.setId(examId);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);

        } catch (Exception e) {
            // Handle any other unexpected exceptions
            response.setSuccess(false);
            response.setMessage("An unexpected error occurred: " + e.getMessage());
            response.setId(examId);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Transactional
    public List<CandidateResponse> getCandidatesByExamId(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with id: " + examId));

        // Return an empty list if there are no candidates
        return exam.getCandidates() == null || exam.getCandidates().isEmpty()
                ? List.of()
                : exam.getCandidates().stream()
                .map(candidate -> new CandidateResponse(
                        candidate.getId(),
                        candidate.getEmail(),
                        candidate.getFirstName(),
                        candidate.getLastName()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CandidateGroupSearchResponse> getCandidateGroupsByOrganizationForSearch(Long organizationId) {

        Set<CandidateGroup> candidateGroups = candidateGroupRepository.findByOrganizationId(organizationId);

        // Return null if no candidate groups are found
        if (candidateGroups.isEmpty()) {
            return null;
        }

        return candidateGroups.stream()
                .map(group -> new CandidateGroupSearchResponse(
                        group.getId(),
                        group.getName(),
                        group.getCandidates().stream()
                                .map(candidate -> new CandidateResponse(
                                        candidate.getId(),
                                        candidate.getEmail(),
                                        candidate.getFirstName(),
                                        candidate.getLastName()))
                                .collect(Collectors.toSet()))) // Collect as a Set
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ConflictExamResponse> getExamsScheduledBetween(Long examId) {
        Exam currentExam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));

        List<Exam> conflictingExams = examRepository.findExamsScheduledBetween(
                currentExam.getOrganization().getId(),
                examId,
                currentExam.getStartDatetime(),
                currentExam.getEndDatetime()
        );

        return conflictingExams.stream()
                .map(exam -> ConflictExamResponse.builder()
                        .id(exam.getId())
                        .title(exam.getTitle())
                        .description(exam.getDescription())
                        .instructions(exam.getInstructions())
                        .duration(exam.getDuration())
                        .startDatetime(exam.getStartDatetime())
                        .endDatetime(exam.getEndDatetime())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CandidateConflictExamResponse> getCandidateConflictingExams(Long examId) {
        Exam currentExam = examRepository.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found with ID: " + examId));

        // Fetch candidates assigned to the current exam
        List<Candidate> assignedCandidates = candidateRepository.findCandidatesAssignedToExamWithConflictingExams(
                examId,
                currentExam.getStartDatetime(),
                currentExam.getEndDatetime()
        );

        List<CandidateConflictExamResponse> responseList = new ArrayList<>();
        for (Candidate candidate : assignedCandidates) {
            // Check the exams of the candidate for conflicts
            candidate.getExams().stream()
                    .filter(exam -> (
                            exam.getStartDatetime().isBefore(currentExam.getEndDatetime()) &&
                                    exam.getEndDatetime().isAfter(currentExam.getStartDatetime())))
                    .forEach(conflictingExam -> responseList.add(CandidateConflictExamResponse.builder()
                            .studentId(candidate.getId())
                            .firstName(candidate.getFirstName())
                            .lastName(candidate.getLastName())
                            .examId(conflictingExam.getId())
                            .title(conflictingExam.getTitle())
                            .description(conflictingExam.getDescription())
                            .instructions(conflictingExam.getInstructions())
                            .duration(conflictingExam.getDuration())
                            .startDatetime(conflictingExam.getStartDatetime())
                            .endDatetime(conflictingExam.getEndDatetime())
                            .build()));
        }

        return responseList; // This will return a list of conflicting exams or empty if none found
    }




}
