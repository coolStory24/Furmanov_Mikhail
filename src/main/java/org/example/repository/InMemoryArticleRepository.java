package org.example.repository;

import org.example.entities.article.Article;
import org.example.entities.article.ArticleId;
import org.example.entities.article.exceptions.ArticleAlreadyExistsException;
import org.example.entities.article.exceptions.ArticleNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryArticleRepository implements ArticleRepository {
  private final Map<ArticleId, Article> articles = new ConcurrentHashMap<>();
  private final AtomicLong currentId = new AtomicLong(0);

  @Override
  public ArticleId generateId() {
    return new ArticleId(currentId.getAndIncrement());
  }

  @Override
  public List<Article> findAll() {
    return new ArrayList<>(articles.values());
  }

  @Override
  public Article findById(ArticleId id) throws ArticleNotFoundException {

    var article = articles.get(id);
    if (article == null) {
      throw new ArticleNotFoundException(id);
    }

    return article;
  }

  public synchronized void create(Article article) throws ArticleAlreadyExistsException {
    if (articles.get(article.getId()) != null) {
      throw new ArticleAlreadyExistsException(article.getId());
    }

    articles.put(article.getId(), article);
  }

  @Override
  public synchronized void update(ArticleId articleId, String name, List<String> tags) throws ArticleNotFoundException {
    var article = findById(articleId);

    articles.put(article.getId(), article.withName(name).withTags(tags));
  }

  @Override
  public synchronized void delete(ArticleId id) throws ArticleNotFoundException {
    if (articles.remove(id) == null) {
      throw new ArticleNotFoundException(id);
    }
  }
}
