package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.example.article.ArticleService;
import org.example.article.SQLArticleRepository;
import org.example.article.controller.ArticleController;
import org.example.article.controller.ArticleTemplateController;
import org.example.comment.CommentService;
import org.example.comment.SQLCommentRepository;
import org.example.comment.controller.CommentController;
import org.example.template.TemplateFactory;
import org.example.transaction.JdbiTransactionManager;
import org.example.transaction.TransactionManager;
import org.flywaydb.core.Flyway;
import org.jdbi.v3.core.Jdbi;
import spark.Service;

import java.util.List;

public class Main {

  public static void main(String[] args) {
    Config config = ConfigFactory.load();

    Flyway flyway =
      Flyway.configure()
        .outOfOrder(true)
        .locations("classpath:db/migrations")
        .dataSource(config.getString("app.database.url"), config.getString("app.database.user"),
          config.getString("app.database.password"))
        .load();
    flyway.migrate();

    Jdbi jdbi = Jdbi.create(config.getString("app.database.url"), config.getString("app.database.user"), config.getString("app.database.password"));
    TransactionManager transactionManager = new JdbiTransactionManager(jdbi);

    Service service = Service.ignite();
    ObjectMapper objectMapper = new ObjectMapper();

    var articleService = new ArticleService(new SQLArticleRepository(jdbi), transactionManager);
    var commentService = new CommentService(new SQLCommentRepository(jdbi), transactionManager, articleService);

    Application application = new Application(
      List.of(
        new ArticleController(
          service,
          objectMapper,
          articleService
        ),
        new CommentController(
          service,
          objectMapper,
          commentService
        ),
        new ArticleTemplateController(
          service,
          articleService,
          TemplateFactory.freeMarkerEngine()
        )
      )
    );

    application.start();
  }
}
