package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Application;
import org.example.controller.response.AddCommentResponse;
import org.example.controller.response.CreateArticleResponse;
import org.example.controller.response.FindArticleResponse;
import org.example.repository.InMemoryArticleRepository;
import org.example.service.ArticleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import spark.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test article controller")
class ArticleControllerTest {
  private Service service;

  @BeforeEach
  void beforeEach() {
    service = Service.ignite();
  }

  @AfterEach
  void afterEach() {
    service.stop();
    service.awaitStop();
  }

  @Test
  @DisplayName("E2E article controller test")
  void shouldUpdateArticle() throws Exception {
    ArticleService articleService = new ArticleService(new InMemoryArticleRepository());
    ObjectMapper objectMapper = new ObjectMapper();
    Application application = new Application(
      List.of(new ArticleController(
        service, objectMapper, articleService
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
    assertEquals(0L, createArticleResponse.id());


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
          .uri(URI.create("http://localhost:%d/api/add-comment/%d".formatted(service.port(), 0L)))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(201, responseToAddingAComment.statusCode());
    AddCommentResponse addCommentResponse =
      objectMapper.readValue(responseToAddingAComment.body(), AddCommentResponse.class);
    assertEquals(0L, addCommentResponse.commentId());

    // update article
    HttpResponse<String> responseToArticleUpdate = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .PUT(
            HttpRequest.BodyPublishers.ofString(
              """
                    {
                      "id": 0,
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
          .uri(URI.create("http://localhost:%d/api/delete-comment?commentId=%d&articleId=%d".formatted(service.port(), 0L, 0L)))
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
          .uri(URI.create("http://localhost:%d/api/article/%d".formatted(service.port(), 0L)))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(200, responseToFindArticle.statusCode());
    FindArticleResponse findArticleResponse =
      objectMapper.readValue(responseToFindArticle.body(), FindArticleResponse.class);
    assertEquals(0L, findArticleResponse.articleId());
    assertTrue(findArticleResponse.tags().contains("weekly"));
    assertFalse(findArticleResponse.tags().contains("daily"));
    assertEquals("Sport news", findArticleResponse.name());
    assertEquals(0, findArticleResponse.comments().size());

    // delete article
    HttpResponse<String> responseToDeleteArticle = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .DELETE()
          .uri(URI.create("http://localhost:%d/api/article/%d".formatted(service.port(), 0L)))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertEquals(204, responseToDeleteArticle.statusCode());
    assertEquals("", responseToDeleteArticle.body());
  }
}