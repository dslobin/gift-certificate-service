package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;

public interface OrderMapper {
    OrderDto toDto(Order order);
}
