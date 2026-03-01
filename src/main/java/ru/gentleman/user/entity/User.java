package ru.gentleman.user.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.gentleman.commom.dto.Role;

import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@Table(schema = "accounts", name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private UUID id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Boolean isEnabled;

    private Boolean isEmailVerified;
}
