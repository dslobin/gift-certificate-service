package com.epam.esm.mapper.impl;

import com.epam.esm.dto.CartDto;
import com.epam.esm.entity.Cart;
import com.epam.esm.mapper.CartMapper;
import org.springframework.stereotype.Component;

@Component
public class CartMapperImpl implements CartMapper {
    @Override
    public Cart toModel(CartDto cartDto) {
        return null;
    }

    @Override
    public CartDto toDto(Cart cart) {
        return null;
    }
}
