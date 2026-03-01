package ru.gentleman.user.query;

import java.util.UUID;

public record FindUserByIdQuery(
        UUID id
) {
}
