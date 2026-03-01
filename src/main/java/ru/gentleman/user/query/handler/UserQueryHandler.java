package ru.gentleman.user.query.handler;

import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.user.dto.UserDto;
import ru.gentleman.user.query.FindUserByEmailQuery;
import ru.gentleman.user.query.FindUserByIdQuery;
import ru.gentleman.user.service.UserService;

@Component
@RequiredArgsConstructor
public class UserQueryHandler {

    private final UserService userService;

    @QueryHandler
    public UserDto on(FindUserByIdQuery query){
        return this.userService.get(query.id());
    }

    @QueryHandler
    public UserDto on(FindUserByEmailQuery query){
        return this.userService.getByEmail(query.email());
    }
}
