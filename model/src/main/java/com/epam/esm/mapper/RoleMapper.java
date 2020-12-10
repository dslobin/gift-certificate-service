package com.epam.esm.mapper;

import com.epam.esm.dto.RoleDto;
import com.epam.esm.entity.Role;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleDto toDto(Role role);

    Set<RoleDto> toDto(Set<Role> roles);
}
