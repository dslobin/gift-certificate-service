package com.epam.esm.mapper;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "cost", source = "price")
    @Mapping(target = "orderId", source = "id")
    @Mapping(target = "firstName", source = "user.firstName")
    @Mapping(target = "lastName", source = "user.lastName")
    OrderDto toDto(Order order);
}
