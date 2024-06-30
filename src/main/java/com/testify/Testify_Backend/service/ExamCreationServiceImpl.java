package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.model.MCQ;
import com.testify.Testify_Backend.model.MCQOption;
import com.testify.Testify_Backend.model.Question;
import com.testify.Testify_Backend.repository.ExamRepository;
import com.testify.Testify_Backend.repository.ExamSetterRepository;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.requests.exam.ExamRequest;
import com.testify.Testify_Backend.requests.exam.OptionRequest;
import com.testify.Testify_Backend.requests.exam.QuestionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamCreationServiceImpl implements ExamCreationService{
    private final ExamRepository examRepository;
    private final ExamSetterRepository examSetterRepository;
    private final OrganizationRepository organizationRepository;

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
                .build();
        examRepository.save(exam);
    }

    @Override
    public void addQuestionsToExam(long exam_id, Set<QuestionRequest> questionRequests){
        Optional<Exam> optionalExam = examRepository.findById(exam_id);
        Exam exam = optionalExam.get();

        Set<Question> questions = questionRequests.stream()
                .map(questionRequest -> mapQuestionRequestToQuestion(questionRequest,exam))
                        .collect(Collectors.toSet());
        exam.setQuestions(questions);
        //exam.setQuestions(questionRequests.stream().map(this::mapQuestionRequestToQuestion).collect(Collectors.toSet()));
        examRepository.save(exam);

    }

    private Question mapQuestionRequestToQuestion(QuestionRequest questionRequest,Exam exam){
        if("MCQ".equals(questionRequest.getQuestionType())){
            MCQ mcq = new MCQ();
            mcq.setQuestion(questionRequest.getQuestion());
            mcq.setExam(examRepository.findById(exam.getId()).get());

            Set<MCQOption> options = questionRequest.getOptionRequests().stream()
                    .map(optionRequest -> mapToMCQoption(optionRequest, mcq))
                    .collect(Collectors.toSet());
            mcq.setOptions(options);

            return mcq;
        }
        else return null;
    }

    private MCQOption mapToMCQoption(OptionRequest optionRequest, MCQ mcq) {
        MCQOption mcqOption = new MCQOption();
        mcqOption.setOptionText(optionRequest.getOptionText());
        mcqOption.setCorrect(optionRequest.isCorrect());
        mcqOption.setMcqQuestion(mcq);

        return mcqOption;
    }

    //TODO::Get Exam
    public Exam getExam(long examId){
        return examRepository.findById(examId).get();
    }

}
