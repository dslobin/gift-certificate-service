package com.epam.esm.dao;

import com.epam.esm.entity.Cart;

public interface CartDao extends CrudDao<Cart, Long> {
    /**
     * Retrieves a cart by user's email.
     *
     * @param userEmail must not be {@literal null}.
     * @return the cart with the given id or {@literal null} if none found.
     */
    Cart findByUserEmail(String userEmail);
}
