package com.example.CRUD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping
//    public List<Book> getAllBooks(){
//        return bookRepository.findAll();
//    }
    public ResponseEntity<List<Book>>getallbooks(){
        List<Book> books = bookRepository.findAll();
        return ResponseEntity.ok(books);
    }

    @PostMapping
    public ResponseEntity<String> CreateBookWithAuthor (@RequestBody BookWithAuthorRequest request){
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setCategory(request.getCategory());
        book.setPrice(request.getPrice());

        Author author = new Author();
        author.setName(request.getName());
        author.setNationality(request.getNationality());

        book.setAuthor(author);

        bookRepository.save(book);
        authorRepository.save(author);

        return ResponseEntity.ok("Book and Author added Successfully");

    }
    @PutMapping
    public Book Updatebook (@PathVariable Long id , @RequestBody Book book) {
        if (bookRepository.existsById(id)) {
            book.setId(id);
            return bookRepository.save(book);
        }
        return null;
    }

    @DeleteMapping
    public void Deletebook (@PathVariable long id){
        bookRepository.deleteById(id);
    }
}
