package org.example.controller;

import org.example.Application;
import org.example.entities.article.Article;
import org.example.entities.article.ArticleId;
import org.example.entities.comment.Comment;
import org.example.entities.comment.CommentId;
import org.example.service.ArticleService;
import org.example.template.TemplateFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Test template article controller")
class ArticleTemplateControllerTest {
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
  @DisplayName("Check static HTML")
  void shouldReturnAppropriateHTML() throws IOException, InterruptedException {
    ArticleService articleService = Mockito.mock(ArticleService.class);
    FreeMarkerEngine freeMarkerEngine = TemplateFactory.freeMarkerEngine();
    Application application = new Application(
      List.of(new ArticleTemplateController(
        service, articleService, freeMarkerEngine
      ))
    );

    Mockito.when(articleService.findAll()
    ).thenReturn(List.of(
        new Article(new ArticleId(0), "Sport news", List.of("sport", "weekly"), List.of(
          new Comment(new CommentId(0), new ArticleId(0), "wow"),
          new Comment(new CommentId(1), new ArticleId(0), "amazing")
        )),
        new Article(new ArticleId(1), "Technology Trends", List.of("tech", "innovation"), List.of(
          new Comment(new CommentId(0), new ArticleId(1), "interesting"),
          new Comment(new CommentId(1), new ArticleId(1), "informative"),
          new Comment(new CommentId(2), new ArticleId(1), "well written")
        )),
        new Article(new ArticleId(2), "Health and Fitness", List.of("health", "exercise"), List.of(
          new Comment(new CommentId(0), new ArticleId(2), "inspiring"),
          new Comment(new CommentId(1), new ArticleId(2), "helpful")
        )),
        new Article(new ArticleId(3), "Travel Destinations", List.of("travel", "adventure"), List.of(
          new Comment(new CommentId(0), new ArticleId(3), "dreamy"),
          new Comment(new CommentId(1), new ArticleId(3), "must-visit"),
          new Comment(new CommentId(2), new ArticleId(3), "beautiful scenery")
        )),
        new Article(new ArticleId(4), "Science Discoveries", List.of("science", "research"), List.of())
      )
    );

    application.start();
    service.awaitInitialization();

    HttpResponse<String> responseToGetStaticHTML = HttpClient.newHttpClient()
      .send(
        HttpRequest.newBuilder()
          .GET()
          .uri(URI.create("http://localhost:%d/".formatted(service.port())))
          .build(),
        HttpResponse.BodyHandlers.ofString(UTF_8)
      );

    assertTrue(responseToGetStaticHTML.body().contains("""
              <td>Technology Trends</td>
              <td>tech, innovation</td>
              <td>3</td>
      """));
  }
}