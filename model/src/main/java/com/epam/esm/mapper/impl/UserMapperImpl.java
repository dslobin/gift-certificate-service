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
        user.setRoles(roleDtoSetToRoleSet(userDto.getRoles()));

        return user;
    }

    private Role roleDtoToRole(RoleDto roleDto) {
        Role role = new Role();

        role.setId(roleDto.getId());
        role.setName(roleDto.getName());

        return role;
    }

    private Set<Role> roleDtoSetToRoleSet(Set<RoleDto> roleDtos) {
        if (roleDtos == null) {
            return null;
        }

        return roleDtos.stream()
                .map(this::roleDtoToRole)
                .collect(Collectors.toSet());
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
        userDto.setRoles(roleSetToRoleDtoSet(user.getRoles()));

        return userDto;
    }

    private RoleDto roleToRoleDto(Role role) {
        RoleDto roleDto = new RoleDto();

        roleDto.setId(role.getId());
        roleDto.setName(role.getName());

        return roleDto;
    }

    private Set<RoleDto> roleSetToRoleDtoSet(Set<Role> roles) {
        if (roles == null) {
            return null;
        }

        return roles.stream()
                .map(this::roleToRoleDto)
                .collect(Collectors.toSet());
    }
}
