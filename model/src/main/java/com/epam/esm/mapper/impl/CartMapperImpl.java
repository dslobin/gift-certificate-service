package com.epam.esm.mapper.impl;

import com.epam.esm.dto.CartDto;
import com.epam.esm.dto.CartItemDto;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.CartItem;
import com.epam.esm.mapper.CartMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CartMapperImpl implements CartMapper {
    @Override
    public CartDto toDto(Cart cart) {
        CartDto cartDto = new CartDto();

        cartDto.setCertificatesCost(cart.getItemsCost());
        cartDto.setItemsCount(cart.getItemsCount());
        String userEmail = cart.getUser().getEmail();
        cartDto.setUserEmail(userEmail);
        cartDto.setCartItems(toCartItemDtoList(cart.getItems(), userEmail));

        return cartDto;
    }

    private CartItemDto toCartItemDto(CartItem cartItem, String userEmail) {
        CartItemDto dto = new CartItemDto();

        dto.setCertificateId(cartItem.getGiftCertificate().getId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setUserEmail(userEmail);

        return dto;
    }

    private List<CartItemDto> toCartItemDtoList(Set<CartItem> cartItems, String userEmail) {
        if (cartItems == null) {
            return null;
        }

        return cartItems.stream()
                .map(cartItem -> toCartItemDto(cartItem, userEmail))
                .collect(Collectors.toList());
    }
}
