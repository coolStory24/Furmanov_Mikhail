package org.example.controller.request;

import java.util.List;

public record CreateArticleRequest(String name, List<String> tags) {
}