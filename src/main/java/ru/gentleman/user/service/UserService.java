package ru.gentleman.user.service;

import ru.gentleman.commom.event.UserCreatedEvent;
import ru.gentleman.commom.event.UserDeletedEvent;
import ru.gentleman.commom.event.UserUpdatedEvent;
import ru.gentleman.user.dto.UserDto;

import java.util.UUID;

public interface UserService {

    UserDto get(UUID id);

    void create(UserCreatedEvent event);

    void update(UserUpdatedEvent event);

    void delete(UserDeletedEvent event);

    UserDto getByEmail(String email);

    Boolean existsUserByEmail(String email);

    Boolean existsById(UUID id);

    void updateEmailConfirmedStatus(String email);

    void updatePassword(String newPassword, String email);
}
