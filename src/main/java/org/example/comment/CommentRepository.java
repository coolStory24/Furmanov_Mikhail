package org.example.comment;

import org.example.exceptions.CommentNotFoundException;

import java.sql.SQLException;

public interface CommentRepository {
  String create(Long articleId, String text);

  Comment findById(String commentId) throws SQLException;

  /**
   * @throws CommentNotFoundException
   */
  void delete(String commentId);
}
