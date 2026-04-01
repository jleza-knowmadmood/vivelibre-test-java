package com.vivelibre.books;

import com.vivelibre.books.loader.BookLoader;
import com.vivelibre.books.model.Book;
import com.vivelibre.books.model.BookPageStats;
import com.vivelibre.books.service.BookService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BooksApplication {

    public static void main(String[] args) throws IOException {
        BookLoader bookLoader = new BookLoader();
        BookService bookService = new BookService();
        List<Book> books = bookLoader.loadBooks("books.json");
        BookPageStats pageStats = bookService.calculatePageStats(books);
        List<Book> filteredBooks = bookService.filterByPagesAndHarry(books);
        List<Book> rowlingBooks = bookService.findBooksByAuthor(books, "J.K. Rowling");
        List<String> sortedTitles = bookService.getTitlesSortedAlphabetically(books);
        Map<String, Long> booksByAuthor = bookService.countBooksByAuthor(books);
        Map<String, String> publicationDates = bookService.formatPublicationDates(books);
        List<String> duplicateAuthors = bookService.findDuplicateAuthors(books);
        List<Book> booksWithoutPublicationTimestamp = bookService.findBooksWithoutPublicationTimestamp(books);
        List<Book> mostRecentBooks = bookService.findMostRecentBooks(books);

        System.out.println("Books loaded: " + books.size());
        System.out.println();

        System.out.println("Point 1 - Books with more than 400 pages and 'Harry' in the title:");
        filteredBooks.forEach(book -> System.out.println(" - " + book.getTitle()));
        System.out.println();

        System.out.println("Point 2 - Books by J.K. Rowling:");
        rowlingBooks.forEach(book -> System.out.println(" - " + book.getTitle()));
        System.out.println();

        System.out.println("Point 3 - Titles sorted alphabetically:");
        sortedTitles.forEach(title -> System.out.println(" - " + title));
        System.out.println();

        System.out.println("Point 3 - Number of books by author:");
        booksByAuthor.forEach((author, count) -> System.out.println(" - " + author + ": " + count));
        System.out.println();

        System.out.println("Point 4 - Publication dates formatted as yyyy-MM-dd:");
        publicationDates.forEach((title, date) -> System.out.println(" - " + title + ": " + date));
        System.out.println();

        System.out.println("Point 5 - Page statistics:");
        System.out.println("Average pages: " + pageStats.averagePages());
        System.out.println("Book with most pages: " + pageStats.bookWithMostPages().getTitle());
        System.out.println("Book with least pages: " + pageStats.bookWithLeastPages().getTitle());
        System.out.println();

        System.out.println("Point 7 (optional) - Duplicate authors:");
        duplicateAuthors.forEach(author -> System.out.println(" - " + author));
        System.out.println();

        System.out.println("Point 7 (optional) - Books without publicationTimestamp:");
        booksWithoutPublicationTimestamp.forEach(book -> System.out.println(" - " + book.getTitle()));
        System.out.println();

        System.out.println("Point 8 (optional) - Most recent books:");
        mostRecentBooks.forEach(book -> System.out.println(" - " + book.getTitle()));
    }
}
