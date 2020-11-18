package com.epam.esm.service;

import com.epam.esm.entity.Cart;
import com.epam.esm.entity.CartItem;

import java.util.List;

public interface CartService {

    /**
     * Returns existing or creates new cart of the specified user.
     *
     * @param userEmail unique identifier of the specified user
     * @return user's cart
     */
    Cart getCartOrCreate(String userEmail);

    /**
     * Adds new item into the specified user cart and saves cart.
     *
     * @param userEmail     unique identifier of the specified user
     * @param certificateId unique identifier of the specified gift certificate
     * @param quantity      of the gift certificates
     * @return updated cart
     */
    Cart addToCart(String userEmail, long certificateId, int quantity);

    /**
     * Adds all the listed items into the specified user cart and saves cart.
     *
     * @param userEmail unique identifier of the specified user
     * @param cartItems to save
     * @return updated cart
     */
    Cart addAllToCart(String userEmail, List<CartItem> cartItems);


    /**
     * Clears the specified user cart.
     *
     * @param userEmail unique identifier of the specified user
     * @return updated cart
     */
    Cart clearCart(String userEmail);
}
