package com.epam.esm.mapper;

import com.epam.esm.dto.UserDto;
import com.epam.esm.entity.User;

public interface UserMapper {
    User toModel(UserDto userDto);

    UserDto toDto(User user);
}
