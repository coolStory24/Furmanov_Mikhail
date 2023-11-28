package org.example.article;

import org.example.article.controller.dto.request.CreateArticleDto;
import org.example.article.exceptions.*;
import org.example.transaction.TransactionManager;
import org.jdbi.v3.core.statement.UnableToExecuteStatementException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ArticleService {

  private final ArticleRepository articleRepository;
  private final TransactionManager transactionManager;

  public ArticleService(ArticleRepository articleRepository, TransactionManager transactionManager) {
    this.articleRepository = articleRepository;
    this.transactionManager = transactionManager;
  }

  public List<Article> findAll() {
    return articleRepository.findAll();
  }

  public Article findById(long id) throws ArticleFindException {
    try {
      return articleRepository.findById(id);
    } catch (Exception e) {
      throw new ArticleFindException("Could not find article with id: " + id, e);
    }
  }

  public void findByIdForUpdate(long id) {
    articleRepository.findByIdForUpdate(id);
  }

  public long create(String name, List<String> tags) throws ArticleCreateException {
    try {
      return articleRepository.create(name, new HashSet<>(tags).stream().toList());
    } catch (RuntimeException e) {
      throw new ArticleCreateException("Cannot create article.", e);
    }
  }

  public List<Long> createMany(List<CreateArticleDto> createArticleDtoList) {
    Set<Long> result = new HashSet<>();
    try {
      return transactionManager.inTransaction(() -> {
        for (CreateArticleDto createArticleDto : createArticleDtoList) {
          Long articleId = this.articleRepository.create(createArticleDto.name(), new HashSet<>(createArticleDto.tags()).stream().toList());
          result.add(articleId);
        }
        return result.stream().toList();
      });
    } catch (UnableToExecuteStatementException e) {
      throw new ArticleCreateException("Cannot create articles", e);
    }
  }

  public void update(long id, String name, List<String> tags) throws ArticleUpdateException {
    try {
      articleRepository.update(id, name, tags);
    } catch (ArticleNotFoundException e) {
      throw new ArticleUpdateException("Cannot update, article with id: " + id + " not found", e);
    }
  }

  public void updateTrending(long id, boolean isTrending) {
    articleRepository.updateTrending(id, isTrending);
  }

  public void delete(long id) throws ArticleDeleteException {
    try {
      articleRepository.delete(id);
    } catch (ArticleNotFoundException e) {
      throw new ArticleDeleteException("Cannot delete article with id: " + id + " not found", e);
    }
  }
}

