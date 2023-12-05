package org.example.article.exceptions;

public class ArticleAlreadyExistsException extends RuntimeException {
  public ArticleAlreadyExistsException(long articleId) {
    super("Article with id '" + articleId + "' already exists");
  }
}
