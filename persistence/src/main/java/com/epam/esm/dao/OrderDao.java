package com.epam.esm.dao;

import com.epam.esm.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderDao extends CrudDao<Order, Long> {
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
     * Retrieves an orders by user's email.
     *
     * @param email unique user identifier
     * @return all orders
     */
    List<Order> findByUserEmail(String email);

    /**
     * Retrieves an order by its id and user's email.
     *
     * @param orderId unique order identifier.
     * @param email   unique user identifier.
     * @return the order with the given id and user's email or {@literal Optional#empty()} if none found.
     */
    Optional<Order> findByIdAndUserEmail(long orderId, String email);
}
