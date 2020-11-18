package com.epam.esm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderItemDto extends RepresentationModel<OrderItemDto> {
    @PositiveOrZero
    private long orderId;

    @PositiveOrZero
    private long certificateId;

    @PositiveOrZero
    private int quantity;
}
