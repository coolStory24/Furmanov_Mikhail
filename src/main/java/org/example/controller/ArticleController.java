package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.example.controller.request.AddCommentRequest;
import org.example.controller.request.CreateArticleRequest;
import org.example.controller.request.UpdateArticleRequest;
import org.example.controller.response.*;
import org.example.entities.article.exceptions.*;
import org.example.service.ArticleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Service;

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
    updateArticle();
    deleteArticle();
    addComment();
    deleteComment();
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
        return objectMapper.writeValueAsString(new FindArticleResponse(article.getId().getId(), article.getName(), article.getTags(), article.getComments()));
      } catch (ArticleFindException e) {
        LOG.warn("Cannot find article", e);
        response.status(HttpStatus.BAD_REQUEST_400);
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
        return objectMapper.writeValueAsString(new CreateArticleResponse(articleId.getId()));
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
        return objectMapper.writeValueAsString(new UpdateArticleResponse());
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
        return objectMapper.writeValueAsString(new DeleteArticleResponse());
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

  private void addComment() {
    service.post("/api/add-comment/:id", (Request request, Response response) -> {
      response.type("application/json");

      String id = request.params(":id");
      String body = request.body();

      AddCommentRequest addCommentRequest = objectMapper.readValue(body, AddCommentRequest.class);

      try {
        var commentId = articleService.addCommentToArticle(Long.parseLong(id), addCommentRequest.text());
        response.status(HttpStatus.CREATED_201);
        LOG.debug("Comment successfully added");
        return objectMapper.writeValueAsString(new AddCommentResponse(commentId.getId()));
      } catch (ArticleNotFoundException e) {
        LOG.warn("Cannot add a new comment", e);
        response.status(HttpStatus.BAD_REQUEST_400);
        return objectMapper.writeValueAsString(new ErrorResponse(e.getMessage()));
      } catch (RuntimeException e) {
        LOG.error("Unhandled error", e);
        response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
        return objectMapper.writeValueAsString(new ErrorResponse("Internal server error"));
      }
    });
  }

  private void deleteComment() {
    service.delete("/api/delete-comment", (Request request, Response response) -> {
      response.type("application/json");

      String articleId = request.queryParams("articleId");
      String commentId = request.queryParams("commentId");

      try {
        articleService.deleteCommentFromArticle(Long.parseLong(articleId), Long.parseLong(commentId));
        response.status(HttpStatus.NO_CONTENT_204);
        LOG.debug("Comment successfully deleted");
        return objectMapper.writeValueAsString(new DeleteCommentResponse());
      } catch (CommentDeleteException e) {
        LOG.warn("Cannot delete a comment", e);
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
