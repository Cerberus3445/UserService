package ru.gentleman.user.query.projection;

import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ru.gentleman.commom.event.UserCreatedEvent;
import ru.gentleman.commom.event.UserDeletedEvent;
import ru.gentleman.commom.event.UserUpdatedEvent;
import ru.gentleman.user.service.UserService;

@Component
@RequiredArgsConstructor
@ProcessingGroup("user-group")
public class UserProjection {

    private final UserService userService;

    @EventHandler
    public void on(UserCreatedEvent event) {
        this.userService.create(event);
    }

    @EventHandler
    public void on(UserUpdatedEvent event) {
        this.userService.update(event);
    }

    @EventHandler
    public void on(UserDeletedEvent event) {
        this.userService.delete(event);
    }
}