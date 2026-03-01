package ru.gentleman.user.command.aggregate;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.gentleman.commom.dto.Role;
import ru.gentleman.commom.event.UserCreatedEvent;
import ru.gentleman.commom.event.UserDeletedEvent;
import ru.gentleman.commom.event.UserUpdatedEvent;
import ru.gentleman.user.command.CreateUserCommand;
import ru.gentleman.user.command.DeleteUserCommand;
import ru.gentleman.user.command.UpdateUserCommand;

import java.util.UUID;

@Aggregate
public class UserAggregate {

    @AggregateIdentifier
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;

    private Boolean isEnabled;

    private Boolean isEmailVerified;

    public UserAggregate() {

    }

    @CommandHandler
    public UserAggregate(CreateUserCommand command, PasswordEncoder passwordEncoder) {
        String encodedPassword = passwordEncoder.encode(command.password());

        UserCreatedEvent event = UserCreatedEvent.builder()
                .id(command.id())
                .firstName(command.firstName())
                .lastName(command.lastName())
                .email(command.email())
                .password(encodedPassword)
                .role(command.role())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(UserCreatedEvent event){
        this.id = event.id();
        this.firstName = event.firstName();
        this.lastName = event.lastName();
        this.email = event.email();
        this.password = event.password();
        this.role = event.role();
        this.isEnabled = true;
        this.isEmailVerified = false;
    }

    @CommandHandler
    public void handle(UpdateUserCommand command) {
        UserUpdatedEvent event = UserUpdatedEvent.builder()
                .id(command.id())
                .firstName(command.firstName())
                .lastName(command.lastName())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(UserUpdatedEvent event) {
        this.firstName = event.firstName();
        this.lastName = event.lastName();
    }

    @CommandHandler
    public void handle(DeleteUserCommand command) {
        UserDeletedEvent event = new UserDeletedEvent(command.id());

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(UserDeletedEvent event) {
        this.isEnabled = false;
    }
}
