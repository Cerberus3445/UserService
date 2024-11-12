package com.cerberus.userservice.service.impl;

import com.cerberus.userservice.dto.UserDto;
import com.cerberus.userservice.exception.UserNotFoundException;
import com.cerberus.userservice.mapper.EntityDtoMapper;
import com.cerberus.userservice.model.User;
import com.cerberus.userservice.repository.UserRepository;
import com.cerberus.userservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityDtoMapper mapper;

    @Override
    @Cacheable(value = "user", key = "#id")
    @Transactional
    public UserDto get(Long id) {
        log.info("get {}", id);
        return this.mapper.toDto(this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    @CacheEvict(value = "user", key = "#id")
    @Transactional
    public void update(Long id, UserDto userDto) {
        log.info("update {}, {}", id, userDto);
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
    @CacheEvict(value = "user", key = "#id")
    @Transactional
    public void delete(Long id) {
        log.info("delete {}", id);
        this.userRepository.deleteById(id);
    }
}
