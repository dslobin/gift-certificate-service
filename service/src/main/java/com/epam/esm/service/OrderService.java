package com.epam.esm.service;

import com.epam.esm.entity.Order;
import com.epam.esm.exception.EmptyCartException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    /**
     * @param pageable  pagination information index
     * @param userEmail unique identifier of the specified user
     * @return orders of the specified user
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    List<Order> findUserOrders(String userEmail, Pageable pageable);

    /**
     * @param userEmail unique identifier of the specified user
     * @return orders of the specified user
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    List<Order> findUserOrders(String userEmail);

    /**
     * @param userEmail unique identifier of the specified user
     * @param orderId   unique identifier of the specified order
     * @return order of the specified user and id
     * @throws OrderNotFoundException if the order with the specified user email and order id doesn't exist
     */
    Order findUserOrder(String userEmail, long orderId);

    /**
     * Creates new order for the specified user.
     *
     * @param userEmail unique identifier of the specified user
     * @return created order
     * @throws EmptyCartException    if the specified user cart is empty
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    Order createUserOrder(String userEmail);
}
