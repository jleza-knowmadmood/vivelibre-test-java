package com.vivelibre.books.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivelibre.books.model.Book;
import com.vivelibre.books.model.BookPageStats;
import com.vivelibre.books.model.BookWithWordCount;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class BookService {

    private static final int WORDS_PER_PAGE = 250;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    // Punto 1: filtrar libros con más de 400 páginas y cuyo título contenga "Harry".
    public List<Book> filterByPagesAndHarry(List<Book> books) {
        return books.stream()
                .filter(book -> book.getPages() > 400 && book.getTitle().contains("Harry"))
                .toList();
    }

    // Punto 2: obtener los libros escritos por "J.K. Rowling".
    public List<Book> findBooksByAuthor(List<Book> books, String authorName) {
        return books.stream()
                .filter(book -> book.getAuthor() != null)
                .filter(book -> authorName.equals(book.getAuthor().fullName()))
                .toList();
    }

    // Punto 3: listar los títulos ordenados alfabéticamente.
    public List<String> getTitlesSortedAlphabetically(List<Book> books) {
        return books.stream()
                .map(Book::getTitle)
                .sorted()
                .toList();
    }

    // Punto 3: contar cuántos libros ha escrito cada autor.
    public Map<String, Long> countBooksByAuthor(List<Book> books) {
        return books.stream()
                .filter(book -> book.getAuthor() != null)
                .collect(Collectors.groupingBy(book -> book.getAuthor().fullName(), Collectors.counting()));
    }

    // Punto 4: convertir publicationTimestamp a formato AAAA-MM-DD.
    public Map<String, String> formatPublicationDates(List<Book> books) {
        return books.stream()
                .filter(book -> book.getPublicationTimestamp() != null && !book.getPublicationTimestamp().isBlank())
                .collect(Collectors.toMap(Book::getTitle, book -> formatTimestamp(book.getPublicationTimestamp())));
    }

    // Punto 5: calcular el promedio de páginas y encontrar el libro con más y menos páginas.
    public BookPageStats calculatePageStats(List<Book> books) {
        return new BookPageStats(
                calculateAveragePages(books),
                findBookWithMostPages(books).orElseThrow(),
                findBookWithLeastPages(books).orElseThrow()
        );
    }

    // Punto 6: añadir el campo wordCount usando 250 palabras por página.
    public List<BookWithWordCount> addWordCount(List<Book> books) {
        return books.stream()
                .map(book -> new BookWithWordCount(
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor().fullName(),
                        book.getPages(),
                        book.getPages() * WORDS_PER_PAGE
                ))
                .toList();
    }

    // Punto 6: agrupar los libros por autor.
    public Map<String, List<Book>> groupBooksByAuthor(List<Book> books) {
        return books.stream()
                .filter(book -> book.getAuthor() != null)
                .collect(Collectors.groupingBy(book -> book.getAuthor().fullName()));
    }

    // Punto 7 (opcional): verificar si hay autores duplicados.
    public List<String> findDuplicateAuthors(List<Book> books) {
        return countBooksByAuthor(books).entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();
    }

    // Punto 7 (opcional): encontrar los libros sin publicationTimestamp.
    public List<Book> findBooksWithoutPublicationTimestamp(List<Book> books) {
        return books.stream()
                .filter(book -> book.getPublicationTimestamp() == null || book.getPublicationTimestamp().isBlank())
                .toList();
    }

    // Punto 8 (opcional): identificar los libros más recientes.
    public List<Book> findMostRecentBooks(List<Book> books) {
        Optional<Long> mostRecentTimestamp = books.stream()
                .map(Book::getPublicationTimestamp)
                .filter(Objects::nonNull)
                .filter(timestamp -> !timestamp.isBlank())
                .map(Long::parseLong)
                .max(Long::compareTo);

        if (mostRecentTimestamp.isEmpty()) {
            return List.of();
        }

        long mostRecent = mostRecentTimestamp.get();
        return books.stream()
                .filter(book -> book.getPublicationTimestamp() != null && !book.getPublicationTimestamp().isBlank())
                .filter(book -> Long.parseLong(book.getPublicationTimestamp()) == mostRecent)
                .toList();
    }

    // Punto 9 (opcional): generar un JSON con títulos y autores.
    public void exportTitlesAndAuthorsToJson(List<Book> books, Path outputPath) throws IOException {
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, buildTitlesAndAuthorsJson(books));
    }

    // Punto 9 (opcional): exportar la lista a CSV con id, title, author_name y pages.
    public void exportBooksToCsv(List<Book> books, Path outputPath) throws IOException {
        Files.createDirectories(outputPath.getParent());
        Files.writeString(outputPath, buildBooksCsv(books));
    }

    private double calculateAveragePages(List<Book> books) {
        return books.stream()
                .mapToInt(Book::getPages)
                .average()
                .orElse(0);
    }

    private Optional<Book> findBookWithMostPages(List<Book> books) {
        return books.stream()
                .max(Comparator.comparingInt(Book::getPages));
    }

    private Optional<Book> findBookWithLeastPages(List<Book> books) {
        return books.stream()
                .min(Comparator.comparingInt(Book::getPages));
    }

    private String formatTimestamp(String rawTimestamp) {
        long epochSeconds = Long.parseLong(rawTimestamp);
        LocalDate publicationDate = Instant.ofEpochSecond(epochSeconds)
                .atZone(ZoneOffset.UTC)
                .toLocalDate();
        return DATE_FORMATTER.format(publicationDate);
    }

    private String buildTitlesAndAuthorsJson(List<Book> books) throws IOException {
        List<Map<String, String>> titlesAndAuthors = books.stream()
                .map(book -> Map.of(
                        "title", book.getTitle(),
                        "author", book.getAuthor().fullName()
                ))
                .toList();

        return OBJECT_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(titlesAndAuthors);
    }

    private String buildBooksCsv(List<Book> books) {
        try {
            StringWriter writer = new StringWriter();
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setHeader("id", "title", "author_name", "pages")
                    .setRecordSeparator(System.lineSeparator())
                    .build();

            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                for (Book book : books) {
                    csvPrinter.printRecord(
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor().fullName(),
                            book.getPages()
                    );
                }
            }

            return writer.toString();
        } catch (IOException exception) {
            throw new IllegalStateException("Error generating CSV export", exception);
        }
    }
}
