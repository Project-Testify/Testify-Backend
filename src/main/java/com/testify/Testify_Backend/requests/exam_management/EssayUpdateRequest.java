package com.testify.Testify_Backend.requests.exam_management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EssayUpdateRequest {
    private long id;
    private String questionText;
    private List<CoverPointRequest> coveringPoints;
}
