package ru.gentleman.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.commom.event.UserCreatedEvent;
import ru.gentleman.commom.event.UserDeletedEvent;
import ru.gentleman.commom.event.UserUpdatedEvent;
import ru.gentleman.commom.util.ExceptionUtils;
import ru.gentleman.user.cache.CacheClear;
import ru.gentleman.user.dto.UserDto;
import ru.gentleman.user.entity.User;
import ru.gentleman.user.mapper.UserMapper;
import ru.gentleman.user.repository.UserRepository;
import ru.gentleman.user.service.UserService;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CacheClear cacheClear;

    @Override
    @Cacheable(value = "user", key = "#id")
    public UserDto get(UUID id) {
        log.info("get {}", id);

        User user = this.userRepository.findByIdAndIsEnabled(id, true)
                .orElseThrow(() -> ExceptionUtils.notFound("error.user.not_found_id", id));

        return this.userMapper.toDto(user);
    }

    @Override
    @Transactional
    public void create(UserCreatedEvent event) {
        log.info("create {}", event);

        User user = this.userMapper.toEntity(event);
        user.setIsEnabled(true);
        user.setIsEmailVerified(false);

        this.userRepository.save(user);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#event.id")
    public void update(UserUpdatedEvent event) {
        log.info("update {}", event);

        this.userRepository.findById(event.id()).ifPresentOrElse(user -> {
            User newUser = User.builder()
                    .id(user.getId())
                    .firstName(event.firstName())
                    .lastName(event.lastName())
                    .password(user.getPassword())
                    .isEnabled(user.getIsEnabled())
                    .isEmailVerified(user.getIsEmailVerified())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            this.userRepository.save(newUser);
            this.cacheClear.clearEmail(user.getEmail());
        }, () -> {
            throw ExceptionUtils.notFound("error.user.not_found_id", event.id());
        });
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#event.id")
    public void delete(UserDeletedEvent event) {
        log.info("delete {}", event);

        User user = this.userRepository.findByIdAndIsEnabled(event.id(), true)
                .orElseThrow(() -> ExceptionUtils.notFound("error.user.not_found_id", event.id()));

        user.setIsEnabled(false);

        this.cacheClear.clearEmail(user.getEmail());
    }

    @Override
    @Cacheable(value = "email", key = "#email")
    public UserDto getByEmail(String email) {
        log.info("getByEmail {}", email);

        return this.userMapper.toDto(this.userRepository.findByEmailAndIsEnabled(email, true)
                .orElseThrow(() -> ExceptionUtils.notFound("error.user.not_found_id", email)));
    }

    @Override
    public Boolean existsUserByEmail(String email) {
        log.info("existsUserByEmail {}", email);

        return this.userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsById(UUID id) {
        log.info("existsById {}", id);

        return this.userRepository.existsById(id);
    }

    @Override
    @Transactional
    public void updateEmailConfirmedStatus(String email) {
        log.info("updateEmailConfirmedStatus {}", email);

        User user = this.userRepository.findByEmailAndIsEnabled(email, true)
                .orElseThrow(() -> ExceptionUtils.notFound("error.user.not_found_id", email));

        user.setIsEmailVerified(true);

        this.cacheClear.clearUserById(user.getId());
        this.cacheClear.clearEmail(email);
    }

    @Override
    @Transactional
    public void updatePassword(String newPassword, String email) {
        log.info("updatePassword {}", email);

        User user = this.userRepository.findByEmailAndIsEnabled(email, true)
                .orElseThrow(() -> ExceptionUtils.notFound("error.user.not_found_id", email));

        this.cacheClear.clearUserById(user.getId());
        this.cacheClear.clearEmail(email);
    }
}
