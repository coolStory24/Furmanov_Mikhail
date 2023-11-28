package org.example.exceptions;

public class CommentNotFoundException extends RuntimeException {

  public CommentNotFoundException(String commentId) {
    super("Comment with id '" + commentId + "' not found");
  }
}
