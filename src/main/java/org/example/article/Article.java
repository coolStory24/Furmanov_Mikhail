package org.example.article;

import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class Article {
  @Nullable
  private final Long id;
  private final String name;
  private final Collection<String> tags;
  private final Boolean trending;
  private final Long comments;

  public Article(@Nullable Long id, String name, Collection<String> tags, Boolean trending, Long comments) {
    this.id = id;
    this.name = name;
    this.tags = tags;
    this.trending = trending;
    this.comments = comments;
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Collection<String> getTags() {
    return tags;
  }

  public Long getComments() {
    return comments;
  }

  public Boolean getTrending() {
    return trending;
  }
}
