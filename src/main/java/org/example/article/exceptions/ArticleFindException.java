package org.example.article.exceptions;

public class ArticleFindException extends RuntimeException {

  public ArticleFindException(String message, Exception e) {
    super(message, e);
  }
}
