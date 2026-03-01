package ru.gentleman.user.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record DeleteUserCommand(
        @TargetAggregateIdentifier
        UUID id
) {
}
