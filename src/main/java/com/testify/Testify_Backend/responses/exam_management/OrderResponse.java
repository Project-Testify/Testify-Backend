package com.testify.Testify_Backend.responses.exam_management;

import com.testify.Testify_Backend.enums.OrderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private OrderType orderType;
    private Integer value;
}
