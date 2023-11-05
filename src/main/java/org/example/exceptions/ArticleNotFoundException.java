package org.example.exceptions;

import org.example.entities.article.ArticleId;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException(ArticleId articleId, RuntimeException e) {
    super("Article with id '" + articleId + "' not found", e);
  }
}
