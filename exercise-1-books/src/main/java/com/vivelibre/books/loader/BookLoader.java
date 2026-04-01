package com.vivelibre.books.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivelibre.books.model.Book;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BookLoader {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Book> loadBooks(String resourceName) throws IOException {
        InputStream inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(resourceName);

        if (inputStream == null) {
            throw new IOException("Resource not found: " + resourceName);
        }

        return objectMapper.readValue(inputStream, new TypeReference<>() {
        });
    }
}
