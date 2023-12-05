package org.example.article.exceptions;

public class ArticleNotFoundException extends RuntimeException {

  public ArticleNotFoundException(long articleId) {
    super("Article with id '" + articleId + "' not found");
  }
}
