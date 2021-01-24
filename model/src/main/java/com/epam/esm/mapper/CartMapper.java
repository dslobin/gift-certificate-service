package com.epam.esm.mapper;

import com.epam.esm.dto.CartDto;
import com.epam.esm.dto.CartItemDto;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface CartMapper {
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "certificatesCost", source = "itemsCost")
    @Mapping(target = "cartItems", source = "items")
    CartDto toDto(Cart cart);

    @Mapping(
            target = "giftCertificate.durationInDays",
            expression = "java( giftCertificate.getDuration() == null ? 0 : giftCertificate.getDuration().toDays() )"
    )
    CartItemDto toCartItemDto(CartItem cartItem);

    List<CartItemDto> toCartItemDtoList(Set<CartItem> items);
}
