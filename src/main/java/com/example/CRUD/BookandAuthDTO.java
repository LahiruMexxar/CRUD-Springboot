package com.example.CRUD;

import java.util.List;

public class BookandAuthDTO {
    private Author author;
    private List<Book> books;

    public BookandAuthDTO(Author author, List<Book> books) {
        this.author = author;
        this.books = books;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }
}
