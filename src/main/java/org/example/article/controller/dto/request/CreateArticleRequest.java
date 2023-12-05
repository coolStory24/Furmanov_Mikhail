package org.example.article.controller.dto.request;

import java.util.List;

public record CreateArticleRequest(String name, List<String> tags) {
}
