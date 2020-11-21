package com.epam.esm.mapper.impl;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
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

        User user = order.getUser();
        orderDto.setOrderId(order.getId());
        orderDto.setUserEmail(user.getEmail());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setCost(order.getPrice());

        return orderDto;
    }
}
