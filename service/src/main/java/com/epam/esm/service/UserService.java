package com.epam.esm.service;

import com.epam.esm.dto.UserDto;

import java.util.List;
import java.util.Optional;

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
     */
    Optional<UserDto> findById(long id);

    /**
     * @param email unique identifier of the specified user
     * @return user associated with the specified email
     */
    Optional<UserDto> findByEmail(String email);
}
