package org.example.article.exceptions;

public class ArticleDeleteException extends RuntimeException {

  public ArticleDeleteException(String message, RuntimeException e) {
    super(message, e);
  }
}
