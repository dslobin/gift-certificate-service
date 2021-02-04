package com.epam.esm.service.impl;

import com.epam.esm.entity.Role;
import com.epam.esm.repository.RoleRepository;
import com.epam.esm.service.RoleService;
import com.epam.esm.service.config.ServiceContextTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith({SpringExtension.class, MockitoExtension.class})
@ContextConfiguration(classes = ServiceContextTest.class)
class RoleServiceImplTest {
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RoleService roleService;

    @Test
    void givenTag_whenFindByName_thenGetCorrectTag() {
        long roleId = 1L;
        String roleName = "ROLE_ADMIN";
        Role role = new Role(roleId, roleName, null);

        given(roleRepository.findByName(roleName)).willReturn(Optional.of(role));

        Optional<Role> roleOptional = roleService.findByName(roleName);
        assertTrue(roleOptional.isPresent());

        Role actualRole = roleOptional.get();
        assertEquals(role.getName(), actualRole.getName());
    }
}
