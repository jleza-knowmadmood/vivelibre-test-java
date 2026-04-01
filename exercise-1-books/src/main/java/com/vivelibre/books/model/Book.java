package com.vivelibre.books.model;

public class Book {

    private int id;
    private String title;
    private String publicationTimestamp;
    private int pages;
    private String summary;
    private Author author;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublicationTimestamp() {
        return publicationTimestamp;
    }

    public void setPublicationTimestamp(String publicationTimestamp) {
        this.publicationTimestamp = publicationTimestamp;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
}
