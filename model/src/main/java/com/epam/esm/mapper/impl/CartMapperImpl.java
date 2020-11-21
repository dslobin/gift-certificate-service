package com.epam.esm.mapper.impl;

import com.epam.esm.dto.CartDto;
import com.epam.esm.dto.CartItemDto;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.CartItem;
import com.epam.esm.mapper.CartMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapperImpl implements CartMapper {
    @Override
    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();

        cartDto.setCertificatesCost(cart.getItemsCost());
        cartDto.setItemsCount(cart.getItemsCount());
        cartDto.setUserEmail(cart.getUser().getEmail());
        List<CartItemDto> cartItems = cart.getItems().stream()
                .map(this::toCartItemDto)
                .collect(Collectors.toList());
        cartDto.setCartItems(cartItems);

        return cartDto;
    }

    private CartItemDto toCartItemDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();

        dto.setCertificateId(cartItem.getGiftCertificate().getId());
        dto.setQuantity(cartItem.getQuantity());

        return dto;
    }
}
