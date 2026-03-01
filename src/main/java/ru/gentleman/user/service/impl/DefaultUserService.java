package ru.gentleman.user.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@Transactional
@RequiredArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final CacheClear cacheClear;

    private final PasswordEncoder passwordEncoder;

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
    public UserDto create(UserDto userDto) {
        log.info("create {}", userDto);

        User user = this.userMapper.toEntity(userDto);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        user.setIsEnabled(true);
        user.setIsEmailVerified(false);

        User createdUser = this.userRepository.save(
                user
        );

        return this.userMapper.toDto(createdUser);
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#id")
    public void update(UUID id, UserDto userDto) {
        log.info("update {}, {}", id, userDto);

        this.userRepository.findById(id).ifPresentOrElse(user -> {
            User newUser = User.builder()
                    .id(user.getId())
                    .firstName(userDto.getFirstName())
                    .lastName(userDto.getLastName())
                    .password(user.getPassword())
                    .isEnabled(user.getIsEnabled())
                    .isEmailVerified(user.getIsEmailVerified())
                    .email(user.getEmail())
                    .role(user.getRole())
                    .build();
            this.userRepository.save(newUser);
        }, () -> {
            throw ExceptionUtils.notFound("error.user.not_found_id", id);
        });

        this.cacheClear.clearEmail(userDto.getEmail());
    }

    @Override
    @Transactional
    @CacheEvict(value = "user", key = "#id")
    public void delete(UUID id) {
        log.info("delete {}", id);

        User user = this.userRepository.findByIdAndIsEnabled(id, true)
                .orElseThrow(() -> ExceptionUtils.notFound("error.user.not_found_id", id));

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

        user.setPassword(this.passwordEncoder.encode(newPassword));

        this.cacheClear.clearUserById(user.getId());
        this.cacheClear.clearEmail(email);
    }
}
