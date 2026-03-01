package ru.gentleman.user.query.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.commom.exception.ValidationException;
import ru.gentleman.commom.util.ValidationErrorUtils;
import ru.gentleman.user.dto.UserDto;
import ru.gentleman.user.service.UserService;
import ru.gentleman.user.validator.UserCreateValidator;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Interaction with user")
public class UserController {

    private final UserService userService;

    private final UserCreateValidator userCreateValidator;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    @Operation(summary = "Получение пользователя.")
    public UserDto get(@PathVariable("id") UUID id){
        return this.userService.get(id);
    }

    @GetMapping(params = "email")
    @Operation(summary = "Получение пользователя по электронной почте.")
    public UserDto getByEmail(@RequestParam("email") String email){
        return this.userService.getByEmail(email);
    }

    @PostMapping
    @Operation(summary = "Создание пользователя.")
    public ResponseEntity<UserDto> create(@RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.userCreateValidator.validate(userDto.getEmail());

        UserDto createdUser = this.userService.create(userDto);

        return ResponseEntity
                .created(URI.create("/api/v1/users/" + createdUser.getId()))
                .body(createdUser);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Обновление пользователя.")
    public ResponseEntity<String> update(@PathVariable("id") UUID id, @RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        this.userService.update(id, userDto);
        return ResponseEntity.status(HttpStatus.OK).body(
                this.messageSource.getMessage(
                        "info.user.updated",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление пользователя.")
    public ResponseEntity<String> delete(@PathVariable("id") UUID id){
        this.userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(
                this.messageSource.getMessage(
                        "info.user.deleted",
                        null,
                        Locale.getDefault()
                )
        );
    }
}