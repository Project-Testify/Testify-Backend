package com.testify.Testify_Backend.responses.exam_management;

import java.time.LocalDateTime;
import java.util.List;

public class ExamResponse {
    private long id;
    private String title;
    private String description;
    private String instructions;
    private int duration;
    private int totalMarks;
    private int passMarks;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private boolean isPrivate;
    private String examSetterName;
    private String organizationName;
    private String moderatorName;
    private List<String> proctorNames;
    private List<String> candidateNames;
    private List<Long> questionSequence;
}
