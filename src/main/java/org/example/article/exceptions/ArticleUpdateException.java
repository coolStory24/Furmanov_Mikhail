package org.example.article.exceptions;

public class ArticleUpdateException extends RuntimeException {

  public ArticleUpdateException(String message, RuntimeException e) {
    super(message, e);
  }
}

