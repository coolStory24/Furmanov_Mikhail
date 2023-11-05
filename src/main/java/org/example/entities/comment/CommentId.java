package org.example.entities.comment;

import java.util.concurrent.atomic.AtomicLong;

public class CommentId {
  private static final AtomicLong currentId = new AtomicLong(0);
  private final long id;

  private CommentId(long id) {
    this.id = id;
  }

  static public CommentId generateId() {
    return new CommentId(currentId.getAndIncrement());
  }

  public long getId() {
    return id;
  }
}
