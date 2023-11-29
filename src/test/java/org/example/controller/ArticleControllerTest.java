package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Application;
import org.example.article.ArticleService;
import org.example.article.SQLArticleRepository;
import org.example.article.controller.ArticleController;
import org.example.article.controller.dto.response.AddCommentResponse;
import org.example.article.controller.dto.response.CreateArticleResponse;
import org.example.article.controller.dto.response.FindArticleResponse;
import org.example.comment.CommentService;
import org.example.comment.SQLCommentRepository;
import org.example.comment.controller.CommentController;
import org.example.transaction.JdbiTransactionManager;
import org.example.transaction.TransactionManager;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test article controller")
@Testcontainers
class ArticleControllerTest {
  private Service service;
  private static Jdbi jdbi;
  private static TransactionManager transactionManager;

  @Container
  public static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:13");

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
  }

  @BeforeAll
  static void beforeAll() {
    String postgresJdbcUrl = POSTGRES.getJdbcUrl();
    Flyway flyway = Flyway.configure().outOfOrder(true).locations("classpath:db/migrations").dataSource(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword()).load();
    flyway.migrate();
    jdbi = Jdbi.create(postgresJdbcUrl, POSTGRES.getUsername(), POSTGRES.getPassword());
    transactionManager = new JdbiTransactionManager(jdbi);
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  @DisplayName("E2E article controller test")
  void shouldUpdateArticle() throws Exception {

    ArticleService articleService = new ArticleService(new SQLArticleRepository(jdbi), transactionManager);
    CommentService commentService = new CommentService(new SQLCommentRepository(jdbi), transactionManager, articleService);
    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application(
      List.of(new ArticleController(
        service, objectMapper, articleService
      ), new CommentController(
        service, objectMapper, commentService
      ))
    );

    application.start();
    service.awaitInitialization();

    // create article
    HttpResponse<String> responseToArticleCreation = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .POST(
            HttpRequest.BodyPublishers.ofString(
              """
                    {
                      "name": "News",
                      "tags": [
                        "daily",
                        "fresh"
                      ]
                    }
                """
            )
          )
          .uri(URI.create("http://localhost:%d/api/article".formatted(service.port())))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(201, responseToArticleCreation.statusCode());
    CreateArticleResponse createArticleResponse =
      objectMapper.readValue(responseToArticleCreation.body(), CreateArticleResponse.class);
    assertEquals(1L, createArticleResponse.id());


    // add a comment
    HttpResponse<String> responseToAddingAComment = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .POST(
            HttpRequest.BodyPublishers.ofString(
              """
                    {
                      "text": "Wow, great news!"
                    }
                """
            )
          )
          .uri(URI.create("http://localhost:%d/api/comment/%d".formatted(service.port(), 1L)))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(201, responseToAddingAComment.statusCode());
    AddCommentResponse addCommentResponse =
      objectMapper.readValue(responseToAddingAComment.body(), AddCommentResponse.class);
    assertTrue(addCommentResponse.commentId().matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"));

    // update article
    HttpResponse<String> responseToArticleUpdate = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .PUT(
            HttpRequest.BodyPublishers.ofString(
              """
                    {
                      "id": 1,
                      "name": "Sport news",
                      "tags": [
                        "sport",
                        "weekly"
                      ]
                    }
                """
            )
          )
          .uri(URI.create("http://localhost:%d/api/article".formatted(service.port())))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(202, responseToArticleUpdate.statusCode());
    assertEquals("{}", responseToArticleUpdate.body());

    // delete comment
    HttpResponse<String> responseToCommentDeletion = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .DELETE()
          .uri(URI.create("http://localhost:%d/api/comment/%s".formatted(service.port(), addCommentResponse.commentId())))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(204, responseToCommentDeletion.statusCode());
    assertEquals("", responseToCommentDeletion.body());

    // get article by id
    HttpResponse<String> responseToFindArticle = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .GET()
          .uri(URI.create("http://localhost:%d/api/article/%d".formatted(service.port(), 1L)))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(200, responseToFindArticle.statusCode());
    FindArticleResponse findArticleResponse =
      objectMapper.readValue(responseToFindArticle.body(), FindArticleResponse.class);
    assertEquals(1L, findArticleResponse.articleId());
    assertTrue(findArticleResponse.tags().contains("weekly"));
    assertFalse(findArticleResponse.tags().contains("daily"));
    assertEquals("Sport news", findArticleResponse.name());
    assertEquals(0, findArticleResponse.comments());

    // delete article
    HttpResponse<String> responseToDeleteArticle = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .DELETE()
          .uri(URI.create("http://localhost:%d/api/article/%d".formatted(service.port(), 1L)))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(204, responseToDeleteArticle.statusCode());
    assertEquals("", responseToDeleteArticle.body());

    // try to find deleted article
    HttpResponse<String> responseToFindDeletedArticle = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .GET()
          .uri(URI.create("http://localhost:%d/api/article/%d".formatted(service.port(), 1L)))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(404, responseToFindDeletedArticle.statusCode());
    assertEquals("{\"message\":\"Could not find article with id: 1\"}", responseToFindDeletedArticle.body());
  }
}