package org.example.repository;

import org.example.entities.article.Article;
import org.example.entities.article.ArticleId;
import org.example.exceptions.ArticleNotFoundException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArticleRepository {
  static private final List<Article> articles = new CopyOnWriteArrayList<Article>();

  static public Article getArticleById(ArticleId id) throws ArticleNotFoundException {
    try {
      return ArticleRepository.articles.stream().filter(article -> article.id == id).findFirst().orElseThrow();
    } catch (NoSuchElementException e) {
      throw new ArticleNotFoundException(id, e);
    }
  }

  synchronized static public void deleteArticleById(ArticleId id) throws ArticleNotFoundException {
    var article = getArticleById(id);
    articles.remove(article);
  }
}
