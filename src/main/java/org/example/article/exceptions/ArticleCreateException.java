package org.example.article.exceptions;

public class ArticleCreateException extends RuntimeException {

  public ArticleCreateException(String message, RuntimeException e) {
    super(message, e);
  }
}


