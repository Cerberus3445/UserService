package ru.gentleman.user.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import ru.gentleman.commom.dto.Role;

import java.util.UUID;

@Builder
public record CreateUserCommand(
        @TargetAggregateIdentifier
        UUID id,
        String firstName,
        String lastName,
        String email,
        String password,
        Role role
) {
}
