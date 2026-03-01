package ru.gentleman.user.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record UpdateUserCommand(
        @TargetAggregateIdentifier
        UUID id,
        String firstName,
        String lastName
) {
}
