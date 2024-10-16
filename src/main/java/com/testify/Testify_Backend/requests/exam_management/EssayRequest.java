package com.testify.Testify_Backend.requests.exam_management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class EssayRequest{
    private long examId;
    private String questionText;
    private String difficultyLevel;
    private List<CoverPointRequest> coveringPoints;
}
