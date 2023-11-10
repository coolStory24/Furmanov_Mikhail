package org.example.exceptions;

import org.example.entities.comment.CommentId;

public class CommentAlreadyExistsException extends RuntimeException {
  public CommentAlreadyExistsException(CommentId commentId) {
    super("Comment with id '" + commentId + "' already exists");
  }
}
