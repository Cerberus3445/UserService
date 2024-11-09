package com.cerberus.userservice.mapper;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoMapper {

    @Autowired
    private ModelMapper mapper;

    public User toEntity(UserDto userDto){
        return this.mapper.map(userDto, User.class);
    }

    public UserDto toDto(User user){
        return this.mapper.map(user, UserDto.class);
    }

}
