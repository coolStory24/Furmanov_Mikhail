package org.example.entities.article;

import org.example.entities.comment.Comment;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Article {
  public ArticleId id;

  public final String name;
  public final Collection<String> tags = Collections.synchronizedCollection(new HashSet<String>());
  public final List<Comment> comments = new CopyOnWriteArrayList<>();


  public Article(String name) {
    this.name = name;
    this.id = ArticleId.generateId();
  }


}
