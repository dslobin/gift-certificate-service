package com.epam.esm.service;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.exception.EmptyCartException;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    /**
     * @param page      current page index
     * @param size      number of items per page
     * @param userEmail unique identifier of the specified user
     * @return orders of the specified user
     */
    List<OrderDto> findUserOrders(int page, int size, String userEmail);

    /**
     * @param userEmail unique identifier of the specified user
     * @param orderId   unique identifier of the specified order
     * @return order of the specified user and id
     */
    Optional<OrderDto> findUserOrder(String userEmail, long orderId);

    /**
     * Creates new order for the specified user.
     *
     * @param userEmail unique identifier of the specified user
     * @return created order
     * @throws EmptyCartException if the specified user cart is empty
     */
    OrderDto createUserOrder(String userEmail);
}
