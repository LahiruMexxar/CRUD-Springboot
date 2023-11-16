package com.example.CRUD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
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
    @Autowired
    private BookService bookService;
    //All GetMapping Methods

    @GetMapping("/filterbyauthor")
    public ResponseEntity<List<Author>>getauthorbynationality(@RequestParam(required = false)String nationality ){
        List<Author> authors;
        if (nationality !=null){
            authors = authorRepository.findByNationality(nationality);
        }else{
            authors = authorRepository.findAll();
        }
        return ResponseEntity.ok(authors);
    }
    @GetMapping("/filterbybook")
    public ResponseEntity<List<Book>>getBookByCategory(@RequestParam(required = false)String category ){
        List<Book> books;
        if (category !=null){
            books = bookRepository.findBookByCategory(category);
        }else{

            books = bookRepository.findAll();
        }
        return ResponseEntity.ok(books);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Book>>getallbooks(){
        List<Book> books = bookRepository.findAll();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    // All PostMapping Methods

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

    @PostMapping ("/addbook")
    public ResponseEntity<ApiResponse<Author>>addAuthorWithBooks(@RequestBody BookandAuthDTO request){
       //Create DTO to add two entities
        Author author = request.getAuthor();
        List<Book> books = request.getBooks();
        //return values to Bookservice
        return bookService.saveAuthorWithBooks(author,books);
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
