package com.epam.esm.service;

import com.epam.esm.entity.Cart;
import com.epam.esm.exception.GiftCertificateNotFoundException;
import com.epam.esm.exception.UserNotFoundException;

public interface CartService {

    /**
     * Returns existing or creates new cart of the specified user.
     *
     * @param userEmail unique identifier of the specified user
     * @return user's cart
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    Cart getCartOrCreate(String userEmail);

    /**
     * Adds new item into the specified user cart and saves cart.
     *
     * @param userEmail     unique identifier of the specified user
     * @param certificateId unique identifier of the specified gift certificate
     * @param quantity      of the gift certificates
     * @return updated cart
     * @throws GiftCertificateNotFoundException if the specified gift certificate does not exist
     * @throws UserNotFoundException            if the user with the specified email doesn't exist
     */
    Cart addToCart(String userEmail, long certificateId, int quantity);

    /**
     * Clears the specified user cart.
     *
     * @param userEmail unique identifier of the specified user
     * @return updated cart
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    Cart clearCart(String userEmail);

    /**
     * Checks if there are any items in the cart.
     */
    boolean isCartEmpty(Cart cart);
}
