package org.example.controller.response;

import org.example.entities.comment.Comment;

import java.util.Collection;
import java.util.List;

public record FindArticleResponse(long articleId, String name,
                                  Collection<String> tags,
                                  List<Comment> comments) {
}
