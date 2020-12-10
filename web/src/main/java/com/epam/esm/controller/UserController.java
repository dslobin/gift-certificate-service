package com.epam.esm.controller;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.UserNotFoundException;
import com.epam.esm.mapper.TagMapper;
import com.epam.esm.mapper.UserMapper;
import com.epam.esm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final TagMapper tagMapper;

    @Value("${pagination.defaultPageValue}")
    private Integer defaultPage;
    @Value("${pagination.maxElementsOnPage}")
    private Integer maxElementsOnPage;

    /**
     * View users.
     *
     * @return users list
     */
    @GetMapping
    public ResponseEntity<Set<UserDto>> getUsers(
            @RequestParam(defaultValue = "${pagination.defaultPageValue}") Integer page,
            @Min(5) @Max(100) @RequestParam(defaultValue = "${pagination.maxElementsOnPage}") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Set<UserDto> users = userService.findAll(pageable).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
        users.forEach(user -> user.add(linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()));
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
        UserDto user = userMapper.toDto(userService.findById(id));
        user.add(
                linkTo(methodOn(UserController.class).getUsers(defaultPage, maxElementsOnPage)).withRel("users"),
                linkTo(methodOn(UserController.class).getUser(user.getId())).withSelfRel()
        );
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
    public ResponseEntity<Set<TagDto>> getMostUsedUserTag(@Min(1) @PathVariable Long id)
            throws UserNotFoundException {
        String userEmail = userService.findById(id).getEmail();
        Set<TagDto> mostUsedUserTags = userService.findMostUsedUserTag(userEmail).stream()
                .map(tagMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(mostUsedUserTags);
    }
}
