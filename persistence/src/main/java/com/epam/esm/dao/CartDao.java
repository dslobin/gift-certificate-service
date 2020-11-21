package com.epam.esm.dao;

import com.epam.esm.entity.Cart;

import java.util.Optional;

public interface CartDao {
    /**
     * Retrieves a cart by its id.
     *
     * @param id must not be {@literal null}.
     * @return the cart with the given id or {@literal Optional#empty()} if none found.
     */
    Optional<Cart> findById(long id);

    /**
     * Saves a given cart.
     *
     * @return the saved cart.
     */
    Cart save(Cart cart);

    /**
     * Retrieves a cart by user's email.
     *
     * @param userEmail must not be {@literal null}.
     * @return the cart with the given id or {@literal null} if none found.
     */
    Cart findByUserEmail(String userEmail);
}
