package com.epam.esm.service.impl;

import com.epam.esm.dao.CartDao;
import com.epam.esm.entity.Cart;
import com.epam.esm.entity.CartItem;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.User;
import com.epam.esm.service.CartService;
import com.epam.esm.service.GiftCertificateService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {
    private final CartDao cartDao;
    private final UserService userService;
    private final GiftCertificateService giftCertificateService;

    //TODO: изменить IllegalArgumentException на свой exception

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Cart getCartOrCreate(String userEmail) {
        User user = userService.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException(userEmail));
        Optional<Cart> existedCart = cartDao.findById(user.getId());
        return existedCart.orElse(createCart(user));
    }

    private Cart createCart(User user) {
        log.debug("Created a shopping cart for a user with id: {}", user.getId());
        Cart cart = new Cart(user);
        return cartDao.save(cart);
    }

    @Override
    @Transactional
    public Cart addToCart(String userEmail, long certificateId, int quantity) {
        Cart cart = getCartOrCreate(userEmail);
        GiftCertificate giftCertificate = giftCertificateService.findById(certificateId)
                .orElseThrow(() -> new IllegalArgumentException(String.valueOf(certificateId)));
        if (giftCertificate.isAvailable()) {
            cart.update(giftCertificate, quantity);
            return cartDao.save(cart);
        }
        return cart;
    }

    @Override
    @Transactional
    public Cart addAllToCart(String userEmail, List<CartItem> cartItems) {
        Cart cart = getCartOrCreate(userEmail);
        AtomicBoolean updated = new AtomicBoolean(false);
        cartItems.forEach(cartItem -> {
            Optional<GiftCertificate> product = giftCertificateService.findById(cartItem.getGiftCertificate().getId());
            if (product.isPresent() && product.get().isAvailable()) {
                cart.update(product.get(), cartItem.getQuantity());
                updated.set(true);
            }
        });
        return updated.get()
                ? cartDao.save(cart)
                : cart;
    }

    @Override
    @Transactional
    public Cart clearCart(String userEmail) {
        Cart cart = getCartOrCreate(userEmail);
        cart.clear();
        return cartDao.save(cart);
    }
}
