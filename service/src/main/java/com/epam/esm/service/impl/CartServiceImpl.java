package com.epam.esm.service.impl;

import com.epam.esm.dao.CartDao;
import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dto.CartDto;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.CartMapper;
import com.epam.esm.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartDao cartDao;
    private final CartMapper cartMapper;
    private final UserDao userDao;
    private final GiftCertificateDao giftCertificateDao;

    @Override
    @Transactional
    public CartDto getCartOrCreate(String userEmail)
            throws UserNotFoundException {
        Cart cart = findUserCartOrCreate(userEmail);
        return cartMapper.toDto(cart);
    }

    private Cart findUserCartOrCreate(String userEmail)
            throws UserNotFoundException {
        User user = userDao.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));
        Optional<Cart> existedCart = cartDao.findById(user.getId());
        Cart cart;
        if (existedCart.isPresent()) {
            cart = existedCart.get();
            log.debug("Cart(id = {}) was found", cart.getId());
        } else {
            cart = new Cart(user);
            cart = cartDao.save(cart);
            log.debug("Cart has been created (id = {})", cart.getId());
        }
        return cart;
    }

    @Override
    @Transactional
    public CartDto addToCart(String userEmail, long certificateId, int quantity)
            throws GiftCertificateNotFoundException, UserNotFoundException {
        CartDto cartDto = getCartOrCreate(userEmail);
        GiftCertificate giftCertificate = giftCertificateDao.findById(certificateId)
                .orElseThrow(() -> new GiftCertificateNotFoundException(certificateId));

        if (giftCertificate.isAvailable()) {
            Cart cart = findUserCartOrCreate(userEmail);
            cart.update(giftCertificate, quantity);
            Cart updatedCart = cartDao.save(cart);
            return cartMapper.toDto(updatedCart);
        }
        return cartDto;
    }

    @Override
    @Transactional
    public CartDto clearCart(String userEmail)
            throws UserNotFoundException {
        Cart cart = findUserCartOrCreate(userEmail);
        cart.clear();
        Cart clearedCart = cartDao.save(cart);
        return cartMapper.toDto(clearedCart);
    }
}
