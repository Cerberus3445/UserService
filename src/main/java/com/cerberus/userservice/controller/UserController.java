package com.cerberus.userservice.controller;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.UserValidationException;
import com.cerberus.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id){
        return this.userService.get(id);
    }

    @PatchMapping("/{id}")
    public HttpStatus update(@PathVariable("id") Long id, @RequestBody @Valid UserDto userDto, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            String errors = bindingResult.getFieldErrors().stream().map(fieldError ->
                    fieldError.getDefaultMessage()).toList().toString();
            throw new UserValidationException(errors);
        }
        this.userService.update(id, userDto);
        return HttpStatus.OK;
    }

    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("id") Long id){
        this.userService.delete(id);
        return HttpStatus.OK;
    }
}
