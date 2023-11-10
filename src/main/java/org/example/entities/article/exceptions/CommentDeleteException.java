package org.example.entities.article.exceptions;

public class CommentDeleteException extends RuntimeException {

  public CommentDeleteException(String message, RuntimeException e) {
    super(message, e);
  }
}
