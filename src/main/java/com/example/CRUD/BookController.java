package com.example.CRUD;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private Services services;
    //All GetMapping Methods


    //Get all books method
    @GetMapping("/getallbooks")
    public ResponseEntity<ApiResponse<List<Book>>>getallBooks(){
        try{
            List<Book> books = bookRepository.findAll();
            ApiResponse<List<Book>> apiResponse = new ApiResponse<>(200,"Books Retrived",books);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }catch (Exception e){
            ApiResponse<List<Book>>errorResponse = new ApiResponse<>(500,"Internal Server Issue",null);
            return new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
    public ResponseEntity<ApiResponse<List<AuthorWithBooksRes>>>getAllAuthorAndBooks(){
        try {
            List<Author> authors = authorRepository.findAll();
            List<AuthorWithBooksRes> authorResponse = new ArrayList<>();

            for (Author author: authors){
                List<Book> books = bookRepository.findByAuthor(author);
                AuthorWithBooksRes response = new AuthorWithBooksRes(author,books);
                authorResponse.add(response);
            }
            ApiResponse<List<AuthorWithBooksRes>> apiResponse = new ApiResponse<>(200,"data Retrieved", authorResponse);
            return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }catch (Exception e){
            ApiResponse<List<AuthorWithBooksRes>> errorresposnse = new ApiResponse<>(500,"An Error Occurred", null);
            return new ResponseEntity<>(errorresposnse, HttpStatus.OK);
        }
    }
//    public ResponseEntity<List<Book>>getallbooks(){
//        List<Book> books = bookRepository.findAll();
//        return new ResponseEntity<>(books, HttpStatus.OK);
//    }

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
        return services.saveAuthorWithBooks(author,books);
    }

    @PostMapping("/{authorId}/addBook")
    public ResponseEntity<ApiResponse<List<Book>>> addBookToAuthor(@PathVariable Long authorId, @RequestBody List<Book> books) {
        try {
            // Check if the author exists
            Optional<Author> authorOptional = authorRepository.findById(authorId);
            if (authorOptional.isEmpty()) {
                throw new NoSuchElementException("Author not found.");
            }

            Author author = authorOptional.get();
            //using Books as an Arraylist
            List<Book> savedbooks = new ArrayList<>();

            for (Book book :books) {
                // Check if a book with the same title already exists for the author
                Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(book.getTitle(), author);
                if (existingBook.isPresent()) {
                    throw new DuplicateKeyException("A book with the same title already exists for the author.");
                }

                // Add the book to the author
                book.setAuthor(author);
                Book savedBook = bookRepository.save(book);
                savedbooks.add(savedBook);
            }

            ApiResponse<List<Book>> apiResponse = new ApiResponse<>(201, "Book added successfully", savedbooks);

            //apiResponse.setPayload(existingBooks);

            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (NoSuchElementException e) {
            ApiResponse<List<Book>> errorResponse = new ApiResponse<>(404, e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (DuplicateKeyException e) {
            ApiResponse<List<Book>> errorResponse = new ApiResponse<>(400, e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<List<Book>> errorResponse = new ApiResponse<>(500, "An error occurred while adding the book", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
