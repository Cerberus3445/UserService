package com.cerberus.userservice.service;

import com.cerberus.userservice.dto.UserDto;

public interface UserService {

    UserDto get(Long id);

    void update(Long id, UserDto userDto);

    void delete(Long id);

}
