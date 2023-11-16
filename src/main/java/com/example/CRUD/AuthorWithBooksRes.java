package com.example.CRUD;

import java.util.List;
import java.util.stream.Collectors;

public class AuthorWithBooksRes {
    private Long authorId;
    private String authorName;
    private List<BookRes> books;

    public AuthorWithBooksRes(Author author, List<Book> books) {
        this.authorId = author.getAid();
        this.authorName = author.getName();
        this.books = books.stream()
                .map(BookRes::new)
                .collect(Collectors.toList());
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public List<BookRes> getBooks() {
        return books;
    }

    public void setBooks(List<BookRes> books) {
        this.books = books;
    }
}
