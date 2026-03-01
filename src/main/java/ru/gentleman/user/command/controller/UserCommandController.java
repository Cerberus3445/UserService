package ru.gentleman.user.command.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.commom.exception.ValidationException;
import ru.gentleman.commom.util.ValidationErrorUtils;
import ru.gentleman.user.command.CreateUserCommand;
import ru.gentleman.user.command.DeleteUserCommand;
import ru.gentleman.user.command.UpdateUserCommand;
import ru.gentleman.user.dto.UserDto;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Command Controller", description = "Взаимодействие с write операциями пользователя")
public class UserCommandController {

    private final CommandGateway commandGateway;

    private final MessageSource messageSource;

    @PostMapping
    @Operation(summary = "Создание пользователя.")
    public ResponseEntity<String> create(@RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        UUID id = UUID.randomUUID();
        CreateUserCommand command = CreateUserCommand.builder()
                .id(id)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .role(userDto.getRole())
                .build();

        this.commandGateway.sendAndWait(command);

        return ResponseEntity
                .created(URI.create("/api/v1/users/" + id))
                .body(
                        this.messageSource.getMessage(
                                "info.user.created",
                                new Object[]{id},
                                Locale.getDefault()
                        )
                );
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление пользователя.")
    public ResponseEntity<String> update(@PathVariable("id") UUID id, @RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        UpdateUserCommand command = UpdateUserCommand.builder()
                .id(id)
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .build();

        this.commandGateway.sendAndWait(command);

        return ResponseEntity
                .ok(
                        this.messageSource.getMessage(
                                "info.user.update",
                                null,
                                Locale.getDefault()
                        )
                );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя.")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id){
        DeleteUserCommand command = new DeleteUserCommand(id);

        this.commandGateway.sendAndWait(command);

        return ResponseEntity.noContent().build();
    }
}
