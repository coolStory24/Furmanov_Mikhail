package org.example.article.controller.dto.request;

import java.util.List;

public record UpdateArticleRequest(long id, String name, List<String> tags) {
}
