package com.cerberus.userservice.exception;

public class UserNotFoundException extends RuntimeException {

  private final static String MESSAGE = "Пользователь с %d id не найден ";

  public UserNotFoundException(Long id) {
    super(MESSAGE.formatted(id));
  }

}
