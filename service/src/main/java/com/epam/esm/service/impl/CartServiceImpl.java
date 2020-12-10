package com.epam.esm.service.impl;

import com.epam.esm.entity.Cart;
import com.epam.esm.entity.CartItem;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.repository.CartRepository;
import com.epam.esm.service.CartService;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserService userService;
    private final GiftCertificateService certificateService;

    @Override
    @Transactional
    public Cart getCartOrCreate(String userEmail)
            throws UserNotFoundException {
        User account = userService.findByEmail(userEmail);
        Optional<Cart> cartOptional = cartRepository.findById(account.getId());
        Cart cart = cartOptional.orElseGet(() -> createCart(account));
        cart.setItemsCost(calculateItemsCost(cart));
        return cart;
    }

    private Cart createCart(User user) {
        log.debug("Creating new cart for user with id = {}", user.getId());
        return cartRepository.save(new Cart(user));
    }

    @Override
    @Transactional
    public Cart addToCart(String userEmail, long certificateId, int quantity)
            throws GiftCertificateNotFoundException, UserNotFoundException {
        Cart cart = getCartOrCreate(userEmail);
        GiftCertificate giftCertificate = certificateService.findById(certificateId);
        if (giftCertificate.isAvailable()) {
            updateCart(cart, giftCertificate, quantity);
            return cartRepository.save(cart);
        }
        log.warn("Gift certificate with id = {} not available. Failed to add item to cart", certificateId);
        return cart;
    }

    @Override
    @Transactional
    public Cart clearCart(String userEmail)
            throws UserNotFoundException {
        Cart cart = getCartOrCreate(userEmail);
        clear(cart);
        return cartRepository.save(cart);
    }


    /**
     * Cart management
     */

    @Override
    public boolean isCartEmpty(Cart cart) {
        return cart.getItems().isEmpty();
    }

    private void clear(Cart cart) {
        cart.setItemsCost(BigDecimal.ZERO);
        cart.getItems().clear();
    }

    private int getItemsCount(Cart cart) {
        return cart.getItems().size();
    }

    private void updateCart(Cart cart, GiftCertificate certificate, int newQuantity) {
        if (certificate == null) {
            return;
        }

        if (newQuantity > 0) {
            CartItem existedItem = findItemById(cart, certificate.getId());
            if (existedItem == null) {
                log.debug("The certificate(id = {}) has been added to the cart", certificate.getId());
                cart.getItems().add(new CartItem(cart, certificate, newQuantity));
            } else {
                log.debug("The number of certificates(id = {}) has been renewed. New quantity: {}", certificate.getId(), newQuantity);
                existedItem.setQuantity(newQuantity);
            }
        } else {
            log.debug("The certificate(id = {}) has been removed from the cart", certificate.getId());
            removeItem(cart, certificate.getId());
        }

        BigDecimal itemsCost = calculateItemsCost(cart);
        cart.setItemsCost(itemsCost);
        log.debug("Cart items cost = {}", itemsCost);
        log.debug("Cart items quantity = {}", getItemsCount(cart));
    }

    private BigDecimal calculateItemsCost(Cart cart) {
        return cart.getItems().stream()
                .map(this::calculateCartItemCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateCartItemCost(CartItem cartItem) {
        BigDecimal itemsCount = BigDecimal.valueOf(cartItem.getQuantity());
        GiftCertificate certificate = cartItem.getGiftCertificate();
        return certificate.getPrice().multiply(itemsCount);
    }

    private CartItem findItemById(Cart cart, long certificateId) {
        return cart.getItems().stream()
                .filter(cartItem -> cartItem.getGiftCertificate().getId() == certificateId)
                .findFirst()
                .orElse(null);
    }

    private void removeItem(Cart cart, long productId) {
        cart.getItems().removeIf(item -> item.getGiftCertificate().getId() == productId);
    }
}
