package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CartDto extends RepresentationModel<CartDto> {
    @Email
    private String userEmail;

    private List<CartItemDto> cartItems = new ArrayList<>();

    @PositiveOrZero
    private int itemsCount;

    @PositiveOrZero
    private BigDecimal certificatesCost;
}
