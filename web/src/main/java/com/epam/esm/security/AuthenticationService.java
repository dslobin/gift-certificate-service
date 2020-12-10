package com.epam.esm.security;

import com.epam.esm.dto.AuthResponse;
import com.epam.esm.dto.SignUpRequest;
import com.epam.esm.entity.User;
import com.epam.esm.exception.EmailExistException;

public interface AuthenticationService {
    /**
     * Authorizes the user.
     *
     * @return created user
     * @throws EmailExistException if the specified user with such email already exist
     */
    AuthResponse authenticate(String email, String password);

    /**
     * Creates a new user.
     *
     * @return created user
     * @throws EmailExistException if the specified user with such email already exist
     */
    User signUp(SignUpRequest userData);
}
