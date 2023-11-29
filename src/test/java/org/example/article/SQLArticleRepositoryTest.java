package org.example.article;

import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
class SQLArticleRepositoryTest {
  @Container
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

  private static Jdbi jdbi;

  @BeforeAll
  static void beforeAll() {
    String postgresJdbcUrl = POSTGRES.getJdbcUrl();
    Flyway flyway = Flyway.configure().outOfOrder(true).locations("classpath:db/migrations").dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword()).load();
    flyway.migrate();
    jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
  }

  @BeforeEach
  void beforeEach() {
    jdbi.useTransaction(handle -> handle.createUpdate("DELETE FROM article").execute());
  }

  @Test
  @DisplayName("Should create article")
  void shouldCreateNewArticle() throws SQLException {
    ArticleRepository repository = new SQLArticleRepository(jdbi);
    var articleName = "new article";
    var articleTags = List.of("news", "daily");

    long articleId = repository.create(articleName, articleTags);
    Article article = repository.findById(articleId);

    for (var tag : article.getTags()) {
      assertTrue(articleTags.contains(tag));
    }
    assertEquals(articleName, article.getName());
    assertEquals(articleId, article.getId());
  }

  @Test
  @DisplayName("Should update article")
  void shouldUpdateArticle() throws SQLException {
    ArticleRepository repository = new SQLArticleRepository(jdbi);
    var articleName = "new article";
    var articleTags = List.of("news", "daily");
    var newName = "super new article";
    var newArticleTags = List.of("weekly", "monthly");

    long articleId = repository.create(articleName, articleTags);
    repository.update(articleId, newName, newArticleTags);

    Article article = repository.findById(articleId);

    for (var tag : article.getTags()) {
      assertTrue(newArticleTags.contains(tag));
    }
    assertEquals(newName, article.getName());
    assertEquals(articleId, article.getId());
  }

  @Test
  @DisplayName("Should delete article")
  void shouldDeleteArticle() {
    ArticleRepository repository = new SQLArticleRepository(jdbi);
    var articleName = "new article";
    var articleTags = List.of("news", "daily");

    long articleId = repository.create(articleName, articleTags);
    repository.delete(articleId);

    Exception exception = assertThrows(RuntimeException.class, () -> repository.findById(articleId));

    String expectedMessage = "Expected at least one element, but found none";
    String actualMessage = exception.getMessage();

    assertEquals(actualMessage, expectedMessage);
  }

  @Test
  @DisplayName("Should create multiple articles")
  void shouldCreateFindAllArticles() {
    ArticleRepository repository = new SQLArticleRepository(jdbi);

    long firstArticleId = repository.create("First article", List.of("first", "news"));
    long secondArticleId = repository.create("Second article", List.of("second", "news"));

    List<Article> articles = repository.findAll();

    assertEquals(2, articles.size());
  }
}