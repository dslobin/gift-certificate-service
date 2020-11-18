package com.epam.esm.dao;

import com.epam.esm.entity.Cart;

import java.util.Optional;

public interface CartDao {
    Optional<Cart> findById(long id);

    Cart save(Cart cart);
}
