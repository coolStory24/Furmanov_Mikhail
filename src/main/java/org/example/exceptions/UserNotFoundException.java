package org.example.exceptions;

public class UserNotFoundException extends RuntimeException {
  String message;

  public UserNotFoundException(String userId) {
    super("User with id '" + userId + "' not found");
  }
}
