package com.epam.esm.mapper.impl;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.User;
import com.epam.esm.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapperImpl implements UserMapper {
    @Override
    public User toModel(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();

        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        Set<Role> roles = userDto.getRoles().stream()
                .map(this::roleDtoToRole)
                .collect(Collectors.toSet());
        user.setRoles(roles);

        return user;
    }

    private Role roleDtoToRole(RoleDto roleDto) {
        Role role = new Role();

        role.setId(roleDto.getId());
        role.setName(roleDto.getName());

        return role;
    }

    @Override
    public UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        Set<RoleDto> roles = user.getRoles().stream()
                .map(this::roleToRoleDto)
                .collect(Collectors.toSet());
        userDto.setRoles(roles);

        return userDto;
    }

    private RoleDto roleToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();

        roleDto.setId(role.getId());
        roleDto.setName(role.getName());

        return roleDto;
    }
}
