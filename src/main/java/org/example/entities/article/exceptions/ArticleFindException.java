package org.example.entities.article.exceptions;

public class ArticleFindException extends RuntimeException {

  public ArticleFindException(String message, RuntimeException e) {
    super(message, e);
  }
}

