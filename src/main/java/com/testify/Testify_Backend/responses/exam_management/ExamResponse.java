package com.testify.Testify_Backend.responses.exam_management;

import com.testify.Testify_Backend.enums.OrderType;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamResponse {
    private long id;
    private String title;
    private String description;
    private String instructions;
    private int duration;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private boolean isPrivate;

    private UserResponse createdBy;
    private OrganizationResponse organization;
    private ExamSetterResponse moderator;
    private Set<ExamSetterResponse> proctors;
    private Set<CandidateResponse> candidates;
    private List<Long> questionSequence;
    private OrderType orderType;
    private FixedOrderResponse fixedOrder;
    private RandomOrderResponse randomOrder;
    private boolean realTimeMonitoring;
    private String zoomLink;
    private boolean browserLockdown;
    private boolean hosted;
}
