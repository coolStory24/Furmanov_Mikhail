package org.example.entities.comment;

import java.util.Objects;

public class CommentId {
  private final long id;

  public CommentId(long id) {
    this.id = id;
  }

  public long getId() {
    return id;
  }

  @Override
  public String toString() {
    return Long.toString(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CommentId commentId = (CommentId) o;
    return id == commentId.id;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
