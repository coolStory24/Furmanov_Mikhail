package org.example.article.controller.dto.response;

import java.util.Collection;

public record FindArticleResponse(long articleId, String name,
                                  Collection<String> tags,
                                  Boolean trending, Long comments) {
}
