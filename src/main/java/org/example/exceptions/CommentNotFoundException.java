package org.example.exceptions;

import org.example.entities.comment.CommentId;

public class CommentNotFoundException extends RuntimeException {

  public CommentNotFoundException(CommentId commentId) {
    super("Comment with id '" + commentId + "' not found");
  }
}
