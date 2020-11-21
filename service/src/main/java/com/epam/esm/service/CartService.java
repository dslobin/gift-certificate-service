package com.epam.esm.service;

import com.epam.esm.dto.CartDto;

public interface CartService {

    /**
     * Returns existing or creates new cart of the specified user.
     *
     * @param userEmail unique identifier of the specified user
     * @return user's cart
     */
    CartDto getCartOrCreate(String userEmail);

    /**
     * Adds new item into the specified user cart and saves cart.
     *
     * @param userEmail     unique identifier of the specified user
     * @param certificateId unique identifier of the specified gift certificate
     * @param quantity      of the gift certificates
     * @return updated cart
     */
    CartDto addToCart(String userEmail, long certificateId, int quantity);

    /**
     * Clears the specified user cart.
     *
     * @param userEmail unique identifier of the specified user
     * @return updated cart
     */
    CartDto clearCart(String userEmail);
}
