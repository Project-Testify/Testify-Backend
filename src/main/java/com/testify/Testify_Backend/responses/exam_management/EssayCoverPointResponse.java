package com.testify.Testify_Backend.responses.exam_management;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EssayCoverPointResponse {
    private long coverPointId; // Cover point ID
    private String coverPointText; // Text of the cover point
    private double marks; // Marks for the cover point
}
