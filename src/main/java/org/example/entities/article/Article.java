package org.example.entities.article;

import org.example.entities.comment.Comment;
import org.example.entities.comment.CommentId;
import org.example.exceptions.CommentAlreadyExistsException;
import org.example.exceptions.CommentNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class Article {
  private final ArticleId id;
  private final String name;
  private final Collection<String> tags;
  private final List<Comment> comments;
  private final AtomicLong currentCommentId = new AtomicLong(0);

  public CommentId generateCommentId() {
    return new CommentId(currentCommentId.getAndIncrement());
  }

  public Article(ArticleId id, String name, Collection<String> tags, List<Comment> comments) {
    this.id = id;
    this.name = name;
    this.tags = tags;
    this.comments = new CopyOnWriteArrayList<>(comments);
  }

  public ArticleId getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Collection<String> getTags() {
    return tags;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public Article withName(String name) {
    return new Article(id, name, tags, comments);
  }

  public Article withTags(List<String> tags) {
    return new Article(id, name, tags, comments);
  }

  public synchronized void addComment(Comment comment) throws CommentAlreadyExistsException {
    if (comments.stream().filter(comment1 -> comment1.getId() == comment.getId()).findFirst().orElse(null) != null) {
      throw new CommentAlreadyExistsException(comment.getId());
    }
    comments.add(comment);
  }

  public synchronized void deleteComment(CommentId commentId) throws CommentNotFoundException {
    var comment = comments.stream().filter(comment1 -> commentId.equals(comment1.getId())).findFirst().orElse(null);

    if (comment == null) {
      throw new CommentNotFoundException(commentId);
    }

    this.comments.remove(comment);
  }
}
