package com.vivelibre.books;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class BooksApplicationTest {

    @Test
    void mainStarts() {
        assertDoesNotThrow(() -> BooksApplication.main(new String[0]));
    }
}
