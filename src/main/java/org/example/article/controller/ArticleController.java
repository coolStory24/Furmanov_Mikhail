package org.example.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.example.article.ArticleService;
import org.example.article.controller.dto.request.CreateArticleRequest;
import org.example.article.controller.dto.request.CreateMultipleArticlesRequest;
import org.example.article.controller.dto.request.UpdateArticleRequest;
import org.example.article.controller.dto.response.CreateArticleResponse;
import org.example.article.controller.dto.response.CreateMultipleArticlesResponse;
import org.example.article.controller.dto.response.ErrorResponse;
import org.example.article.controller.dto.response.FindArticleResponse;
import org.example.article.exceptions.ArticleCreateException;
import org.example.article.exceptions.ArticleDeleteException;
import org.example.article.exceptions.ArticleFindException;
import org.example.article.exceptions.ArticleUpdateException;
import org.example.controller.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.List;

public class ArticleController implements Controller {
  private static final Logger LOG = LoggerFactory.getLogger(ArticleController.class);
  private final Service service;
  private final ObjectMapper objectMapper;
  private final ArticleService articleService;


  public ArticleController(Service service, ObjectMapper objectMapper, ArticleService articleService) {
    this.service = service;
    this.objectMapper = objectMapper;
    this.articleService = articleService;
  }

  @Override
  public void initializeEndpoints() {
    getArticles();
    getArticleById();
    createArticle();
    createMultipleArticles();
    updateArticle();
    deleteArticle();
  }

  private void getArticles() {
    service.get("/api/article", (Request request, Response response) -> {
      response.type("application/json");
      LOG.debug("Articles successfully received");
      return objectMapper.writeValueAsString(articleService.findAll());
    });
  }

  private void getArticleById() {
    service.get("/api/article/:id", (Request request, Response response) -> {
      response.type("application/json");
      String id = request.params(":id");

      try {
        var articleId = Long.parseLong(id);
        var article = articleService.findById(articleId);

        response.status(HttpStatus.OK_200);
        LOG.debug("Article successfully received");
        return objectMapper.writeValueAsString(new FindArticleResponse(article.getId(), article.getName(), article.getTags(), article.getTrending(), article.getComments()));
      } catch (ArticleFindException e) {
        LOG.warn("Cannot find article", e);
        response.status(HttpStatus.NOT_FOUND_404);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("Unhandled error", e);
        response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        return objectMapper.writeValueAsString(new ErrorResponse("Internal server error"));
      }
    });
  }

  private void createArticle() {
    service.post("/api/article", (Request request, Response response) -> {
      response.type("application/json");
      String body = request.body();
      CreateArticleRequest createArticleRequest = objectMapper.readValue(body, CreateArticleRequest.class);

      try {
        var articleId = articleService.create(createArticleRequest.name(), createArticleRequest.tags());
        response.status(HttpStatus.CREATED_201);
        LOG.debug("Articles successfully created");
        return objectMapper.writeValueAsString(new CreateArticleResponse(articleId));
      } catch (ArticleCreateException e) {
        LOG.warn("Cannot create article", e);
        response.status(HttpStatus.BAD_REQUEST_400);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("Unhandled error", e);
        response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        return objectMapper.writeValueAsString(new ErrorResponse("Internal server error"));
      }
    });
  }

  private void createMultipleArticles() {
    service.post("/api/article/create-many", (Request request, Response response) -> {
      response.type("application/json");
      String body = request.body();
      CreateMultipleArticlesRequest createMultipleArticlesRequest = objectMapper.readValue(body, CreateMultipleArticlesRequest.class);

      try {
        List<Long> articleIdList = articleService.createMany(createMultipleArticlesRequest.articles());
        response.status(HttpStatus.CREATED_201);
        LOG.debug("Articles successfully created");
        return objectMapper.writeValueAsString(new CreateMultipleArticlesResponse(articleIdList));
      } catch (ArticleCreateException e) {
        LOG.warn("Cannot create article", e);
        response.status(HttpStatus.BAD_REQUEST_400);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("Unhandled error", e);
        response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        return objectMapper.writeValueAsString(new ErrorResponse("Internal server error"));
      }
    });
  }

  private void updateArticle() {
    service.put("/api/article", (Request request, Response response) -> {
      response.type("application/json");
      String body = request.body();
      UpdateArticleRequest createArticleRequest = objectMapper.readValue(body, UpdateArticleRequest.class);

      try {
        articleService.update(createArticleRequest.id(), createArticleRequest.name(), createArticleRequest.tags());
        response.status(HttpStatus.ACCEPTED_202);
        LOG.debug("Articles successfully updated");
        return "{}";
      } catch (ArticleUpdateException e) {
        LOG.warn("Cannot update article", e);
        response.status(HttpStatus.BAD_REQUEST_400);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("Unhandled error", e);
        response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        return objectMapper.writeValueAsString(new ErrorResponse("Internal server error"));
      }
    });
  }

  private void deleteArticle() {
    service.delete("/api/article/:id", (Request request, Response response) -> {
      response.type("application/json");
      String id = request.params(":id");

      try {
        articleService.delete(Long.parseLong(id));
        response.status(HttpStatus.NO_CONTENT_204);
        LOG.debug("Articles successfully deleted");
        return response;
      } catch (ArticleDeleteException e) {
        LOG.warn("Cannot delete article", e);
        response.status(HttpStatus.BAD_REQUEST_400);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("Unhandled error", e);
        response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        return objectMapper.writeValueAsString(new ErrorResponse("Internal server error"));
      }
    });
  }
}
