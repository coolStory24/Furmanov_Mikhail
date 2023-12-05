package org.example.article.controller.dto.response;

import java.util.List;

public record CreateMultipleArticlesResponse(List<Long> articlesId) {
}
