package com.testify.Testify_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("ESSAY")
public class EssayAnswer extends CandidateExamAnswer {

    @Column(name = "answer_text")
    private String answerText;
}
