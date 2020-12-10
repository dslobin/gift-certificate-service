package com.epam.esm.service;

import com.epam.esm.dto.SignUpRequest;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EmailExistException;
import com.epam.esm.exception.UserNotFoundException;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {
    /**
     * @param pageable pagination information
     * @return list of users
     */
    Set<User> findAll(Pageable pageable);

    /**
     * @param id unique identifier of the specified user
     * @return user associated with the specified id
     * @throws UserNotFoundException if the user with the specified id doesn't exis
     */
    User findById(long id);

    /**
     * @param email unique identifier of the specified user
     * @return user associated with the specified email
     * @throws UserNotFoundException if the user with the specified email doesn't exist
     */
    User findByEmail(String email);

    /**
     * Creates a new user.
     *
     * @return created user
     * @throws EmailExistException if the specified user with such email already exist
     */
    User create(SignUpRequest userData);

    /**
     * Get the most widely used tag of a user
     * with the highest cost of all orders.
     *
     * @param email unique identifier of the specified user
     * @return tags associated with the specified user order
     */
    Set<Tag> findMostUsedUserTag(String email);
}
