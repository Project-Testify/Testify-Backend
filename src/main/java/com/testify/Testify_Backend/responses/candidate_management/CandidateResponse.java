package com.testify.Testify_Backend.responses.candidate_management;

import jdk.jfr.Name;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CandidateResponse {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

}
