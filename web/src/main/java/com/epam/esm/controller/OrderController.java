package com.epam.esm.controller;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.EmptyCartException;
import com.epam.esm.exception.OrderNotFoundException;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.service.OrderService;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    /**
     * View orders.
     *
     * @return orders list of the specified user
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    @GetMapping
    public ResponseEntity<List<OrderDto>> getOrders(
            @Min(1) @RequestParam(required = false, defaultValue = "1") Integer page,
            @Min(5) @Max(100) @RequestParam(required = false, defaultValue = "10") Integer size,
            @RequestBody UserDto userDto
    ) throws UserNotFoundException {
        String userEmail = userDto.getEmail();
        List<OrderDto> orders = orderService.findUserOrders(page, size, userEmail);
        orders.forEach(order -> order.add(linkTo(methodOn(OrderController.class)
                .getOrder(order.getOrderId(), userDto))
                .withSelfRel()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orders);
    }

    /**
     * Viewing a single order.
     *
     * @return order of the specified user
     * @throws OrderNotFoundException if the order with the specified id nad user email doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(
            @PathVariable Long id,
            @RequestBody UserDto userDto
    ) {
        String userEmail = userDto.getEmail();
        OrderDto order = orderService.findUserOrder(userEmail, id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(order);
    }

    /**
     * Order creation.
     *
     * @return created order
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     * @throws EmptyCartException    if the cart is empty
     */
    @RequestMapping("/payment")
    public ResponseEntity<OrderDto> createOrder(
            @RequestBody UserDto userDto
    ) throws EmptyCartException {
        String userEmail = userDto.getEmail();
        OrderDto order = orderService.createUserOrder(userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(order);
    }
}
