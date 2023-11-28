package org.example.exceptions;

public class CommentAlreadyExistsException extends RuntimeException {
  public CommentAlreadyExistsException(long commentId) {
    super("Comment with id '" + commentId + "' already exists");
  }
}
