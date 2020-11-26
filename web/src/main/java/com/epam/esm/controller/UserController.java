package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.UserNotFoundException;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;

    /**
     * View users.
     *
     * @return users list
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @Min(1) @RequestParam(defaultValue = "1") Integer page,
            @Min(5) @Max(100) @RequestParam(defaultValue = "10") Integer size
    ) {
        List<UserDto> users = userService.findAll(page, size);
        users.forEach(user -> user.add(linkTo(methodOn(UserController.class)
                .getUser(user.getId()))
                .withSelfRel()));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(users);
    }

    /**
     * Viewing a single user.
     *
     * @return user with the specified id
     * @throws UserNotFoundException if the user with the specified id doesn't exist
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@Min(1) @PathVariable Long id)
            throws UserNotFoundException {
        UserDto user = userService.findById(id);
        user.add(linkTo(methodOn(UserController.class)
                .getUsers(1, 10))
                .withRel("users"));
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(user);
    }

    /**
     * Get the most widely used tag of a user with the highest cost of all orders.
     *
     * @return tag associated with the specified user order
     * @throws UserNotFoundException if the user with the specified id doesn't exist
     */
    @GetMapping("/{id}/tag")
    public ResponseEntity<TagDto> getMostUsedUserTag(@Min(1) @PathVariable Long id)
            throws UserNotFoundException {
        String userEmail = userService.findById(id).getEmail();
        TagDto tag = userService.findMostUsedUserTag(userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tag);
    }
}
