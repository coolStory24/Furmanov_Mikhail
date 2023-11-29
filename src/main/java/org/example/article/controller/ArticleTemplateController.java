package org.example.article.controller;

import org.example.article.Article;
import org.example.article.ArticleService;
import org.example.controller.Controller;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Service;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArticleTemplateController implements Controller {
  private final Service service;
  private final ArticleService articleService;
  private final FreeMarkerEngine freeMarkerEngine;

  public ArticleTemplateController(Service service, ArticleService articleService, FreeMarkerEngine freeMarkerEngine) {
    this.service = service;
    this.articleService = articleService;
    this.freeMarkerEngine = freeMarkerEngine;
  }

  @Override
  public void initializeEndpoints() {
    getAllArticles();
  }

  private void getAllArticles() {
    service.get("/", (Request request, Response response) -> {
      response.type("text/html; charset=utf-8");
      List<Article> articles = articleService.findAll();
      List<Map<String, String>> articleMapList = articles.stream().map(
        article -> Map.of(
          "name", article.getName(),
          "comments", Long.toString(article.getComments()),
          "tags", String.join(", ", article.getTags()))
      ).toList();

      Map<String, Object> model = new HashMap<>();
      model.put("articles", articleMapList);
      return freeMarkerEngine.render(new ModelAndView(model, "index.ftl"));
    });
  }
}
