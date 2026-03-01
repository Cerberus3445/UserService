package ru.gentleman.user.query.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.user.dto.UserDto;
import ru.gentleman.user.service.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User Query Controller", description = "Взаимодействие с query операциями пользователя")
public class UserQueryController {

    private final UserService userService;

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
}