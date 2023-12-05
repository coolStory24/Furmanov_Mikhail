package org.example.controller;

import org.example.Application;
import org.example.article.Article;
import org.example.article.ArticleService;
import org.example.article.controller.ArticleTemplateController;
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
import java.util.UUID;

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

    UUID[] commentIds = {
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID(),
      UUID.randomUUID()
    };

    Mockito.when(articleService.findAll()
    ).thenReturn(List.of(
        new Article(0L, "Sport news", List.of("sport", "weekly"), false, 0L),
        new Article(1L, "Technology Trends", List.of("tech", "innovation"), true, 3L),
        new Article(2L, "Health and Fitness", List.of("health", "exercise"), false, 2L),
        new Article(3L, "Travel Destinations", List.of("travel", "adventure"), true, 3L),
        new Article(4L, "Science Discoveries", List.of("science", "research"), false, 0L)
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

    System.out.println(responseToGetStaticHTML.body());

    assertTrue(responseToGetStaticHTML.body().contains("""
              <td>Technology Trends</td>
              <td>tech, innovation</td>
              <td>3</td>
      """));
  }
}