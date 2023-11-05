package org.example.entities.article;

import java.util.concurrent.atomic.AtomicLong;

public class ArticleId {
  private static final AtomicLong currentId = new AtomicLong(0);
  private final long id;

  private ArticleId(long id) {
    this.id = id;
  }

  static public ArticleId generateId() {
    return new ArticleId(currentId.getAndIncrement());
  }

  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return Long.toString(id);
  }
}
