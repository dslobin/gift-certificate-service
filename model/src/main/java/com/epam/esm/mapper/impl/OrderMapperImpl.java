package com.epam.esm.mapper.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.mapper.OrderMapper;
import org.springframework.stereotype.Component;

@Component
public class OrderMapperImpl implements OrderMapper {

    @Override
    public OrderDto toDto(Order order) {
        if (order == null) {
            return null;
        }
        OrderDto orderDto = new OrderDto();

        orderDto.setOrderId(order.getId());
        orderDto.setUserEmail(order.getUser().getEmail());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setCost(order.getPrice());

        return orderDto;
    }
}
