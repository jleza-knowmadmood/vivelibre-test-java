package com.vivelibre.books.model;

public record BookPageStats(
        double averagePages,
        Book bookWithMostPages,
        Book bookWithLeastPages
) {}
