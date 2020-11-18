package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDto {
    private String userEmail;
    private long orderId;
    private ZonedDateTime createdAt;
    private BigDecimal cost;
}
