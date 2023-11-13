package org.example.controller.request;

import java.util.List;

public record UpdateArticleRequest(long id, String name, List<String> tags) {
}
