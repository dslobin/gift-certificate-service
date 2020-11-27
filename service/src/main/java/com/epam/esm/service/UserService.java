package com.epam.esm.service;

import com.epam.esm.dto.TagDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.exception.UserNotFoundException;

import java.util.List;
import java.util.Set;

public interface UserService {
    /**
     * @param page current page index
     * @param size number of items per page
     * @return list of users
     */
    List<UserDto> findAll(int page, int size);

    /**
     * @param id unique identifier of the specified user
     * @return user associated with the specified id
     * @throws UserNotFoundException if the user with the specified id doesn't exis
     */
    UserDto findById(long id);

    /**
     * @param email unique identifier of the specified user
     * @return user associated with the specified email
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    UserDto findByEmail(String email);

    /**
     * Get the most widely used tag of a user
     * with the highest cost of all orders.
     *
     * @param email unique identifier of the specified user
     * @return tags associated with the specified user order
     */
    Set<TagDto> findMostUsedUserTag(String email);
}
