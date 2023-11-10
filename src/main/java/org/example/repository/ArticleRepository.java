package org.example.repository;

import org.example.entities.article.Article;
import org.example.entities.article.ArticleId;
import org.example.entities.article.exceptions.ArticleNotFoundException;

import java.util.List;

public interface ArticleRepository {
  ArticleId generateId();

  List<Article> findAll();

  /**
   * @throws ArticleNotFoundException
   */
  Article findById(ArticleId articleId);

  /**
   * @throws ArticleNotFoundException
   */
  void create(Article article);

  /**
   * @throws ArticleNotFoundException
   */
  void update(ArticleId id, String name, List<String> tags);

  /**
   * @throws ArticleNotFoundException
   */
  void delete(ArticleId articleId);
}
