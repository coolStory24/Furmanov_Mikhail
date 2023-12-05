package org.example.article;

import org.example.article.exceptions.ArticleNotFoundException;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultBearing;
import org.postgresql.jdbc.PgArray;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SQLArticleRepository implements ArticleRepository {
  private final Jdbi jdbi;

  public SQLArticleRepository(Jdbi jdbi) {
    this.jdbi = jdbi;
  }

  @Override
  public List<Article> findAll() {
    return jdbi.inTransaction((Handle handle) -> handle.createQuery("SELECT a.id, a.name, a.tags, a.trending, COUNT(c.id) AS comments " + "FROM article a " + "LEFT JOIN comment c ON c.\"articleId\" = a.id " + "GROUP BY a.id").mapToMap().map(result -> {
      var tags = (PgArray) result.get("tags");

      List<String> tagsList;
      try {
        tagsList = Arrays.stream(((String[]) tags.getArray())).toList();
      } catch (SQLException e) {
        throw new RuntimeException(e);
      }
      return new Article((Long) result.get("id"), (String) result.get("name"), tagsList, (Boolean) result.get("trending"), (Long) result.get("comments"));
    }).list());
  }

  @Override
  public Article findById(long articleId) throws SQLException {
    return jdbi.inTransaction((Handle handle) -> {
      Map<String, Object> result = handle.createQuery("SELECT a.id, a.name, a.tags, a.trending, COUNT(c.id) AS comments " + "FROM article a " + "LEFT JOIN comment c ON c.\"articleId\" = a.id " + "WHERE a.id = :id " + "GROUP BY a.id").bind("id", articleId).mapToMap().first();
      var tags = (PgArray) result.get("tags");
      var tagsList = Arrays.stream(((String[]) tags.getArray())).toList();
      return new Article((Long) result.get("id"), (String) result.get("name"), tagsList, (Boolean) result.get("trending"), (Long) result.get("comments"));
    });
  }

  @Override
  public void findByIdForUpdate(long articleId) {
    try {
      jdbi.inTransaction((Handle handle) -> {
        Map<String, Object> result = handle.createQuery(
            "SELECT a.id, a.name, a.trending " +
              "FROM article a " + "WHERE a.id = :id " + "FOR UPDATE;"
          )
          .bind("id", articleId).mapToMap().first();
        return new Article((Long) result.get("id"), (String) result.get("name"), new ArrayList<>(), (Boolean) result.get("trending"), null);
      });
    } catch (IllegalStateException e) {
      throw new ArticleNotFoundException(articleId);
    }
  }

  @Override
  public long create(String name, List<String> tags) {
    return jdbi.inTransaction((Handle handle) -> {
      ResultBearing resultBearing = handle.createUpdate("INSERT INTO article (name, tags) " + "VALUES (:name, :tags)").bind("name", name).bind("tags", tags.toArray(new String[0])).executeAndReturnGeneratedKeys("id");
      Map<String, Object> mapResult = resultBearing.mapToMap().first();
      return ((Long) mapResult.get("id"));
    });
  }

  @Override
  public void updateTrending(long id, boolean trending) {
    jdbi.inTransaction((Handle handle) -> {
      return handle.createUpdate("UPDATE article " + "SET " + "trending = :trending " + "WHERE article.id = :id").bind("trending", trending).bind("id", id).execute();
    });
  }

  @Override
  public void update(long id, String name, List<String> tags) {
    var rowsAffected = jdbi.inTransaction((Handle handle) -> {
      return handle.createUpdate("UPDATE article " + "SET " + "name = :name, " + "tags = :tags " + "WHERE article.id = :id").bind("id", id).bind("name", name).bind("tags", tags.toArray(new String[0])).execute();
    });

    if (rowsAffected == 0) {
      throw new ArticleNotFoundException(id);
    }
  }

  @Override
  public void delete(long articleId) {
    var rowsAffected = jdbi.inTransaction(handle -> {
      return handle.createUpdate("DELETE FROM article WHERE id = :id").bind("id", articleId).execute();
    });

    if (rowsAffected == 0) {
      throw new ArticleNotFoundException(articleId);
    }
  }
}
