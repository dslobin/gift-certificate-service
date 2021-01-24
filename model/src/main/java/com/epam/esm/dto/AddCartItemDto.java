package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode
public class AddCartItemDto {
    @PositiveOrZero
    private long certificateId;

    @PositiveOrZero
    private int quantity;
}
