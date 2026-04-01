package com.vivelibre.books;

import com.vivelibre.books.loader.BookLoader;
import com.vivelibre.books.model.Book;
import com.vivelibre.books.service.BookService;

import java.io.IOException;
import java.util.List;

public class BooksApplication {

    public static void main(String[] args) throws IOException {
        BookLoader bookLoader = new BookLoader();
        BookService bookService = new BookService();
        List<Book> books = bookLoader.loadBooks("books.json");

        System.out.println("Books loaded: " + books.size());
        System.out.println("Books by J.K. Rowling: " + bookService.findBooksByAuthor(books, "J.K. Rowling").size());
        System.out.println("Average pages: " + bookService.calculateAveragePages(books));
    }
}
