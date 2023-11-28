package org.example.comment;

import org.example.article.ArticleService;
import org.example.article.exceptions.ArticleNotFoundException;
import org.example.comment.exceptions.CommentCreateException;
import org.example.comment.exceptions.CommentDeleteException;
import org.example.exceptions.CommentNotFoundException;
import org.example.transaction.TransactionManager;

public class CommentService {
  private final CommentRepository commentRepository;
  private final TransactionManager transactionManager;
  private final ArticleService articleService;

  public CommentService(CommentRepository commentRepository, TransactionManager transactionManager, ArticleService articleService) {
    this.commentRepository = commentRepository;
    this.transactionManager = transactionManager;
    this.articleService = articleService;
  }

  public String create(long articleId, String text) throws CommentCreateException {
    try {
      return transactionManager.inTransaction(() -> {
        // apply pessimistic locking
        articleService.findByIdForUpdate(articleId);

        var article = articleService.findById(articleId);
        if (article.getComments() >= 2) {
          articleService.updateTrending(articleId, true);
        }

        return commentRepository.create(articleId, text);
      });
    } catch (ArticleNotFoundException e) {
      throw e;
    } catch (RuntimeException e) {
      throw new CommentCreateException("Cannot create comment.", e);
    }
  }

  public void delete(String id) throws CommentDeleteException {
    try {
      var comment = commentRepository.findById(id);
      var articleId = comment.getArticleId();
      if (articleId == null) {
        throw new CommentNotFoundException(id);
      }
      // apply pessimistic locking
      articleService.findByIdForUpdate(articleId);

      var article = articleService.findById(articleId);
      if (article.getComments() <= 3) {
        articleService.updateTrending(articleId, false);
      }

      commentRepository.delete(id);
    } catch (Exception e) {
      throw new CommentDeleteException("Cannot delete comment.", e);
    }
  }
}
