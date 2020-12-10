package com.epam.esm.service;

import com.epam.esm.entity.Role;

import java.util.Optional;

public interface RoleService {
    /**
     * @param name unique identifier of the specified role
     * @return role associated with the specified name
     */
    Optional<Role> findByName(String name);
}
