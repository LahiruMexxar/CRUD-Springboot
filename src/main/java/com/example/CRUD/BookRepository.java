package com.example.CRUD;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {
    Optional<Book>findByTitleAndAuthor(String title,Author author);
    List<Book>findBookByCategory(String category);
}

