package org.example.entities.article.exceptions;

import org.example.entities.article.ArticleId;

public class ArticleAlreadyExistsException extends RuntimeException {
  public ArticleAlreadyExistsException(ArticleId articleId) {
    super("Article with id '" + articleId + "' already exists");
  }
}
