package com.vivelibre.books.model;

public record BookWithWordCount(
        int id,
        String title,
        String authorName,
        int pages,
        int wordCount
) {}
