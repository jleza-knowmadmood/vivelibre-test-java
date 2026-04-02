package com.vivelibre.books.service;

import com.vivelibre.books.loader.BookLoader;
import com.vivelibre.books.model.Book;
import com.vivelibre.books.model.BookPageStats;
import com.vivelibre.books.model.BookWithWordCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.assertThat;

class BookServiceTest {

    private BookService bookService;
    private List<Book> books;
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        bookService = new BookService();
        books = new BookLoader().loadBooks("books.json");
    }

    @Test
    void filtersBooksWithMoreThan400PagesAndHarryInTitle() {
        List<Book> filteredBooks = bookService.filterByPagesAndHarry(books);

        assertThat(filteredBooks)
                .extracting(Book::getTitle)
                .containsExactly("Harry Potter and the Deathly Hallows");
    }

    @Test
    void findsBooksByJkRowling() {
        List<Book> rowlingBooks = bookService.findBooksByAuthor(books, "J.K. Rowling");

        assertThat(rowlingBooks)
                .extracting(Book::getTitle)
                .containsExactlyInAnyOrder(
                        "Harry Potter and the Sorcerer's Stone",
                        "Harry Potter and the Deathly Hallows"
                );
    }

    @Test
    void returnsTitlesSortedAlphabetically() {
        List<String> sortedTitles = bookService.getTitlesSortedAlphabetically(books);

        assertThat(sortedTitles).isSorted();
        assertThat(sortedTitles).first().isEqualTo("Harry Potter and the Deathly Hallows");
    }

    @Test
    void countsBooksByAuthor() {
        Map<String, Long> booksByAuthor = bookService.countBooksByAuthor(books);

        assertAll(
                () -> assertThat(booksByAuthor.get("J.K. Rowling")).isEqualTo(2L),
                () -> assertThat(booksByAuthor.get("Suzanne Collins")).isEqualTo(1L),
                () -> assertThat(booksByAuthor.get("Stephen King")).isEqualTo(1L)
        );
    }

    @Test
    void formatsPublicationDatesAsIsoDate() {
        Map<String, String> publicationDates = bookService.formatPublicationDates(books);

        assertAll(
                () -> assertThat(publicationDates.get("Harry Potter and the Sorcerer's Stone")).isEqualTo("1997-06-26"),
                () -> assertThat(publicationDates.get("The Help")).isEqualTo("2009-02-10")
        );
    }

    @Test
    void calculatesPageStatistics() {
        BookPageStats pageStats = bookService.calculatePageStats(books);

        assertAll(
                () -> assertThat(pageStats.averagePages()).isEqualTo(528.125),
                () -> assertThat(pageStats.bookWithMostPages().getTitle()).isEqualTo("The Stand"),
                () -> assertThat(pageStats.bookWithLeastPages().getTitle()).isEqualTo("Harry Potter and the Sorcerer's Stone")
        );
    }

    @Test
    void addsWordCountToEachBook() {
        List<BookWithWordCount> booksWithWordCount = bookService.addWordCount(books);

        assertThat(booksWithWordCount)
                .filteredOn(book -> book.title().equals("The Stand"))
                .singleElement()
                .satisfies(book -> assertThat(book.wordCount()).isEqualTo(288000));
    }

    @Test
    void groupsBooksByAuthor() {
        Map<String, List<Book>> groupedBooks = bookService.groupBooksByAuthor(books);

        assertThat(groupedBooks.get("J.K. Rowling")).hasSize(2);
        assertThat(groupedBooks.get("Harper Lee")).hasSize(1);
    }

    @Test
    void findsDuplicateAuthors() {
        List<String> duplicateAuthors = bookService.findDuplicateAuthors(books);

        assertThat(duplicateAuthors).containsExactly("J.K. Rowling");
    }

    @Test
    void findsBooksWithoutPublicationTimestamp() {
        List<Book> booksWithoutPublicationTimestamp = bookService.findBooksWithoutPublicationTimestamp(books);

        assertThat(booksWithoutPublicationTimestamp)
                .extracting(Book::getTitle)
                .containsExactlyInAnyOrder("The Hunger Games", "To Kill a Mockingbird");
    }

    @Test
    void findsMostRecentBooks() {
        List<Book> mostRecentBooks = bookService.findMostRecentBooks(books);

        assertThat(mostRecentBooks)
                .extracting(Book::getTitle)
                .containsExactly("The Help");
    }

    @Test
    void exportsTitlesAndAuthorsToJsonFile() throws IOException {
        Path outputPath = tempDir.resolve("books-summary.json");

        bookService.exportTitlesAndAuthorsToJson(books, outputPath);

        String exportedJson = Files.readString(outputPath);

        assertThat(exportedJson)
                .contains("\"title\" : \"The Hunger Games\"")
                .contains("\"author\" : \"Suzanne Collins\"")
                .contains("\"title\" : \"Harry Potter and the Sorcerer's Stone\"")
                .contains("\"author\" : \"J.K. Rowling\"");
    }

    @Test
    void exportsBooksToCsvFile() throws IOException {
        Path outputPath = tempDir.resolve("books.csv");

        bookService.exportBooksToCsv(books, outputPath);

        String exportedCsv = Files.readString(outputPath);

        assertThat(exportedCsv)
                .startsWith("id,title,author_name,pages")
                .contains("1,The Hunger Games,Suzanne Collins,374")
                .contains("2,Harry Potter and the Sorcerer's Stone,J.K. Rowling,309");
    }
}
