package com.example.CRUD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.PrimitiveIterator;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    public ResponseEntity<ApiResponse<Author>>saveAuthorWithBooks(Author author, List<Book> books) {

        try {
            //Checking if an Author with the same name already exists
            if (authorRepository.findByName(author.getName()).isPresent()) {
                throw new DuplicateKeyException("Author already exists");
            }
            //Save the Author

            Author savedAuthor = authorRepository.save(author);

            //Link books with the saved Author and also check if there are duplicate book titles

            for (Book book : books) {
                Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(),savedAuthor);

                if (existingBook.isPresent()){
                  //Handiling the duplicate titles
                    throw new DuplicateKeyException("A book with the title'"+ book.getTitle()+ "'already exists");
                }else{
             //add books with the saved author
                book.setAuthor(savedAuthor);
                bookRepository.save(book); }
            }
            return new ResponseEntity<>(new ApiResponse<>(200, "Author and Books Created", savedAuthor), HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(new ApiResponse<>(500, e.getMessage(), null), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(new ApiResponse<>(400, "An Error Occured" ), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

