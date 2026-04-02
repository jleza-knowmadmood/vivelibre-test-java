package com.vivelibre.books.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AuthorTest {

    @Test
    void rejectsNullName() {
        Author author = new Author();

        assertThatThrownBy(() -> author.setName(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Author name must not be null");
    }

    @Test
    void rejectsBlankName() {
        Author author = new Author();

        assertThatThrownBy(() -> author.setName("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Author name must not be blank");
    }

    @Test
    void fullNameFallsBackToSurnameWhenNameIsMissing() {
        Author author = new Author();
        author.setFirstSurname("Rowling");

        assertThat(author.fullName()).isEqualTo("Rowling");
    }
}
