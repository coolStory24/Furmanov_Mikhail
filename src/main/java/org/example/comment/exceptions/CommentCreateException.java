package org.example.comment.exceptions;

public class CommentCreateException extends RuntimeException {

  public CommentCreateException(String message, RuntimeException e) {
    super(message, e);
  }
}
