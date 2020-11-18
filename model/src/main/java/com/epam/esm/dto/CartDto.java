package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDto {
    private String userEmail;
    private List<CartItemDto> cartItems = new ArrayList<>();
    private int itemsCount;
    private BigDecimal certificatesCost;
}
