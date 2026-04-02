package com.vivelibre.books.model;

import java.util.Objects;

public class Author {

    private String name;
    private String firstSurname;
    private String bio;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Author name must not be null");
        if (this.name.isBlank()) {
            throw new IllegalArgumentException("Author name must not be blank");
        }
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname) {
        this.firstSurname = firstSurname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String fullName() {
        if (name == null || name.isBlank()) {
            return firstSurname;
        }
        if (firstSurname == null || firstSurname.isBlank()) {
            return name;
        }
        return name + " " + firstSurname;
    }
}
