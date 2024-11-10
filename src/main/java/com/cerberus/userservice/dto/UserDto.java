package com.cerberus.userservice.dto;

import com.cerberus.userservice.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {

    private Long id;

    @NotNull(message = "Имя не может быть пустым")
    @Length(min = 2, max = 30, message = "Минимальная длина имени составляет 2 символа, максимальная - 30 символов")
    private String firstName;

    @NotNull(message = "Фамилия не может быть пустой")
    @Length(min = 2, max = 30, message = "Минимальная длина фамилия составляет 2 символа, максимальная - 30 символов")
    private String lastName;

    @NotNull(message = "Email не может быть пустым")
    @Email
    @Max(value = 50, message = "Максимальная длина email составляет 50 символов")
    private String email;

    @NotNull(message = "Роль не может быть пустой")
    private Role role;
}
