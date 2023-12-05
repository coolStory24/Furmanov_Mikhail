package org.example.comment;

import org.example.article.exceptions.ArticleNotFoundException;
import org.example.exceptions.CommentNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

public class SQLCommentRepository implements CommentRepository {
  private final Jdbi jdbi;

  public SQLCommentRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public String create(Long articleId, String text) {
    try {
      return jdbi.inTransaction((Handle handle) -> {
        ResultBearing resultBearing = handle.createUpdate("INSERT INTO comment (\"articleId\", text) " + "VALUES (:articleId, :text)").bind("articleId", articleId).bind("text", text).executeAndReturnGeneratedKeys("id");
        Map<String, Object> mapResult = resultBearing.mapToMap().first();
        return (mapResult.get("id").toString());
      });
    } catch (UnableToExecuteStatementException e) {
      throw new ArticleNotFoundException(articleId);
    }
  }

  @Override
  public Comment findById(String commentId) throws SQLException {
    return jdbi.inTransaction((Handle handle) -> {
      Map<String, Object> result = handle.createQuery("SELECT id, text, \"articleId\"" + "FROM comment " + "WHERE comment.id = :id").bind("id", UUID.fromString(commentId)).mapToMap().first();
      System.out.println(result);
      return new Comment(result.get("id").toString(), (Long) result.get("articleid"), result.get("text").toString());
    });
  }

  @Override
  public void delete(String commentId) {
    var rowsAffected = jdbi.inTransaction(handle -> handle.createUpdate("DELETE FROM comment WHERE id = :id").bind("id", UUID.fromString(commentId)).execute());

    if (rowsAffected == 0) {
      throw new CommentNotFoundException(commentId);
    }
  }
}
