package com.testify.Testify_Backend.requests.exam_management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RealTimeMonitoringRequest {
    private boolean realTimeMonitoring;
    private String zoomLink;
}
