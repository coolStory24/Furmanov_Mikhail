package org.example.comment.exceptions;

public class CommentDeleteException extends Exception {

  public CommentDeleteException(String message, Exception e) {
    super(message, e);
  }
}
