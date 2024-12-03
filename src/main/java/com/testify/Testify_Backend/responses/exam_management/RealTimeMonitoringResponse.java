package com.testify.Testify_Backend.responses.exam_management;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RealTimeMonitoringResponse {
    private boolean realTimeMonitoring;
    private String zoomLink;
}
