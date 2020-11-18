package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class CartItemDto extends RepresentationModel<CartItemDto> {
    @PositiveOrZero
    private long certificateId;

    @PositiveOrZero
    private int quantity;

    @Email
    private String userEmail;
}
