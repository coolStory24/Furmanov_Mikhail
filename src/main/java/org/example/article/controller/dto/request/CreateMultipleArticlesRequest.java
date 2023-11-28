package org.example.article.controller.dto.request;

import java.util.List;

public record CreateMultipleArticlesRequest(List<CreateArticleDto> articles) {
}
