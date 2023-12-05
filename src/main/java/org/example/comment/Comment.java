package org.example.comment;

import org.jetbrains.annotations.Nullable;

public class Comment {
  @Nullable
  private final String id;
  @Nullable
  private final Long articleId;
  private final String text;

  public Comment(@Nullable String id, @Nullable Long articleId, String text) {
    this.id = id;
    this.articleId = articleId;
    this.text = text;
  }

  public @Nullable String getId() {
    return id;
  }

  public @Nullable Long getArticleId() {
    return articleId;
  }

  public @Nullable String getText() {
    return text;
  }
}
