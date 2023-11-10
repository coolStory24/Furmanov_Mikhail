package org.example.service;

import org.example.entities.article.Article;
import org.example.entities.article.ArticleId;
import org.example.entities.article.exceptions.*;
import org.example.entities.comment.Comment;
import org.example.entities.comment.CommentId;
import org.example.exceptions.CommentNotFoundException;
import org.example.repository.ArticleRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ArticleService {

  private final ArticleRepository articleRepository;

  public ArticleService(ArticleRepository articleRepository) {
    this.articleRepository = articleRepository;
  }

  public List<Article> findAll() {
    return articleRepository.findAll();
  }

  public Article findById(long id) throws ArticleFindException {
    try {
      var articleId = new ArticleId(id);
      return articleRepository.findById(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleFindException("Article with id=" + id + " not found.", e);
    }
  }

  public ArticleId create(String name, List<String> tags) throws ArticleCreateException {
    var articleId = articleRepository.generateId();
    Article article = new Article(articleId, name, new HashSet<>(tags), new ArrayList<>());
    try {
      articleRepository.create(article);
    } catch (RuntimeException e) {
      throw new ArticleCreateException("Cannot create article.", e);
    }
    return articleId;
  }

  public void update(long id, String name, List<String> tags) throws ArticleUpdateException {
    var articleId = new ArticleId(id);

    try {
      articleRepository.update(articleId, name, tags);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Cannot update article with id: " + articleId, e);
    }
  }

  public void delete(long id) throws ArticleDeleteException {
    var articleId = new ArticleId(id);
    try {
      articleRepository.delete(articleId);
    } catch (ArticleNotFoundException e) {
      throw new ArticleDeleteException("Cannot delete article with id: " + articleId, e);
    }
  }

  public CommentId addCommentToArticle(long id, String text) throws ArticleNotFoundException {
    var articleId = new ArticleId(id);
    var article = articleRepository.findById(articleId);
    var commentId = article.generateCommentId();
    var comment = new Comment(commentId, articleId, text);
    article.addComment(comment);

    return commentId;
  }

  public void deleteCommentFromArticle(long articleId, long commentId) throws CommentDeleteException {
    try {
      var article = articleRepository.findById(new ArticleId(articleId));
      article.deleteComment(new CommentId(commentId));
    } catch (ArticleNotFoundException e) {
      throw new CommentDeleteException("Article with id: " + articleId + " not found.", e);
    } catch (CommentNotFoundException e) {
      throw new CommentDeleteException("Comment with id: " + articleId + " not found.", e);
    }
  }
}

