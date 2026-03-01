package ru.gentleman.user.validator.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.gentleman.commom.util.ExceptionUtils;
import ru.gentleman.user.service.UserService;
import ru.gentleman.user.validator.UserCreateValidator;

@Component
@RequiredArgsConstructor
public class UserCreateValidatorImpl implements UserCreateValidator {

    private final UserService userService;

    @Override
    public void validate(String email){
        if(this.userService.existsUserByEmail(email)){
            ExceptionUtils.alreadyExists("error.details.already_exist", email);
        }
    }
}
