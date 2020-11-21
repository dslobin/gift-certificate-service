package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    /**
     * Retrieves an orders by user's email.
     *
     * @param email unique user identifier
     * @param page  position of the first result numbered from 0.
     * @param size  maximum number of results to retrieve.
     * @return all orders
     */
    List<Order> findByUserEmail(int page, int size, String email);

    /**
     * Retrieves an order by its id and user's email.
     *
     * @param orderId unique order identifier.
     * @param email   unique user identifier.
     * @return the order with the given id and user's email or {@literal Optional#empty()} if none found.
     */
    Optional<Order> findByIdAndUserEmail(long orderId, String email);

    /**
     * Saves a given tag.
     */
    void save(Order order);
}
