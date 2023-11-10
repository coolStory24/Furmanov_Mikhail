package org.example.entities.article.exceptions;

import org.example.entities.article.ArticleId;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException(ArticleId articleId) {
    super("Article with id '" + articleId + "' not found");
  }
}
