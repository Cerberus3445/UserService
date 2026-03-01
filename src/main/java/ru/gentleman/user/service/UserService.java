package ru.gentleman.user.service;

import ru.gentleman.commom.service.CrudOperations;
import ru.gentleman.user.dto.UserDto;

import java.util.UUID;

public interface UserService extends CrudOperations<UserDto, UUID> {

    UserDto getByEmail(String email);

    Boolean existsUserByEmail(String email);

    Boolean existsById(UUID id);

    void updateEmailConfirmedStatus(String email);

    void updatePassword(String newPassword, String email);
}
