package org.example.entities.comment;

import org.example.entities.article.ArticleId;

public class Comment {
  public final CommentId id;
  public final ArticleId articleId;
  public final String text;

  public Comment(ArticleId articleId, String text) {
    this.id = CommentId.generateId();
    this.articleId = articleId;
    this.text = text;
  }
}
