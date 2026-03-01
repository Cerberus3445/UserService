package ru.gentleman.user.mapper;

import org.mapstruct.Mapper;
import ru.gentleman.user.dto.UserDto;
import ru.gentleman.user.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User entity);

    User toEntity(UserDto dto);
}