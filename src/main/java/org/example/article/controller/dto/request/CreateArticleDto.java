package org.example.article.controller.dto.request;

import java.util.List;

public record CreateArticleDto(String name, List<String> tags) {
}
