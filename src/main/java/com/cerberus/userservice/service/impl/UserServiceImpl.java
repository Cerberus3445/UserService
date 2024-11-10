package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.UserNotFoundException;
import com.cerberus.userservice.mapper.EntityDtoMapper;
import com.cerberus.userservice.model.User;
import com.cerberus.userservice.repository.UserRepository;
import com.cerberus.userservice.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityDtoMapper mapper;

    @Override
    @Transactional
    public UserDto get(Long id) {
        return this.mapper.toDto(this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    @Transactional
    public void update(Long id, UserDto userDto) {
        this.userRepository.findById(id).ifPresentOrElse(user -> {
            User mapperUser = this.mapper.toEntity(userDto);
            mapperUser.setId(id);
            mapperUser.setPassword(user.getPassword());
            this.userRepository.save(mapperUser);
        }, () -> {
            throw new UserNotFoundException(id);
        });
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }
}
