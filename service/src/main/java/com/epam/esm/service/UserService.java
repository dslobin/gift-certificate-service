package com.epam.esm.service;

import com.epam.esm.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll(int page, int size);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);
}
