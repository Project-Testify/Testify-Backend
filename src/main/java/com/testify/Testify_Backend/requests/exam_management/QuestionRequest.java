package com.testify.Testify_Backend.requests.exam_management;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.testify.Testify_Backend.enums.QuestionType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "questionType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MCQRequest.class, name = "MCQ"),
        @JsonSubTypes.Type(value = EssayRequest.class, name = "ESSAY")
})
public abstract class QuestionRequest {
    private String questionText;
    private QuestionType type;
}

