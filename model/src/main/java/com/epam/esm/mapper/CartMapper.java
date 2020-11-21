package com.epam.esm.mapper;

import com.epam.esm.dto.CartDto;
import com.epam.esm.entity.Cart;

public interface CartMapper {
    CartDto toDto(Cart cart);
}
