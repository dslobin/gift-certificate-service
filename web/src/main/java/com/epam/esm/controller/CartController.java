package com.epam.esm.controller;

import com.epam.esm.dto.AddCartItemDto;
import com.epam.esm.dto.CartDto;
import com.epam.esm.entity.Cart;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.CartMapper;
import com.epam.esm.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final CartService cartService;
    private final CartMapper cartMapper;

    /**
     * Viewing the cart.
     *
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    @GetMapping
    public ResponseEntity<CartDto> getCart(
            Principal principal
    ) throws UserNotFoundException {
        String userEmail = principal.getName();
        log.debug("Principal name in get cart: {}", userEmail);
        Cart cart = cartService.getCartOrCreate(userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartMapper.toDto(cart));
    }

    /**
     * Adding a gift certificate.
     *
     * @return updated cart
     * @throws GiftCertificateNotFoundException if the specified gift certificate does not exist
     * @throws UserNotFoundException            if the user with the specified email doesn't exist
     */
    @PutMapping
    public ResponseEntity<CartDto> addItem(
            @RequestBody AddCartItemDto item,
            Principal principal
    ) throws GiftCertificateNotFoundException, UserNotFoundException {
        String userEmail = principal.getName();
        log.debug("Principal name in add item to cart: {}", userEmail);
        Cart cart = cartService.addToCart(userEmail, item.getCertificateId(), item.getQuantity());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartMapper.toDto(cart));
    }

    /**
     * Clearing the cart.
     *
     * @return cleared cart
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    @DeleteMapping
    public ResponseEntity<CartDto> clearCart(
            Principal principal
    ) throws UserNotFoundException {
        String userEmail = principal.getName();
        log.debug("Principal name in clear cart: {}", userEmail);
        Cart clearedCart = cartService.clearCart(userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartMapper.toDto(clearedCart));
    }
}
