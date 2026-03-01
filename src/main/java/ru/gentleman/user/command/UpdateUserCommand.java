package ru.gentleman.user.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Builder
public record UpdateUserCommand(
        @TargetAggregateIdentifier
        UUID id,
        String firstName,
        String lastName
) {
}
