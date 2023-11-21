package com.example.CRUD;

import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping("/getauthor")
    public ResponseEntity<ApiResponse<List<Author>>>findallauthor(){
        try {
            //getting all data of author to a list
            List<Author> authors = authorRepository.findAll();
            //using Api response to display data
            ApiResponse<List<Author>> apiResponse = new ApiResponse<>(200,"Authors retrieved",authors);
        return new ResponseEntity<>(apiResponse,HttpStatus.OK);
        }catch (Exception e){
            ApiResponse<List<Author>> errorResponse = new ApiResponse<>(500,"An error occurred while retrieving authors",null);
            return  new ResponseEntity<>(errorResponse,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //used for filtering data

    //request Param
    @GetMapping("/filterbyauthor")
    public ResponseEntity<List<Author>>getauthorbynationality(@RequestParam(required = false)String nationality ){
        //Add Authors as a List
        List<Author> authors;
        // Checking if the nationality is present in the database
        if (nationality !=null){
            authors = authorRepository.findByNationality(nationality);
        }else{
            authors = authorRepository.findAll();
        }
        //display data
        return ResponseEntity.ok(authors);
    }
    @PostMapping("/addauthor")
    public ResponseEntity<ApiResponse<Author>> addAuthor(@RequestBody Author author) {
        try {
            // Check if an author with the same name already exists
            if (authorRepository.findByName(author.getName()).isPresent()) {
                throw new DuplicateKeyException("Author with the same name already exists.");
            }
            // Save the author
            Author savedAuthor = authorRepository.save(author);

            ApiResponse<Author> apiResponse = new ApiResponse<>(201, "Author added successfully", savedAuthor);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            ApiResponse<Author> errorResponse = new ApiResponse<>(400, e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<Author> errorResponse = new ApiResponse<>(500, "An error occurred while adding the author", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping
    public Author UpdateAuthor (@PathVariable Long id, @RequestBody Author author){
        if(authorRepository.existsById(id)){
            author.setId(id);
            return authorRepository.save(author);
        }
        return null;
    }

    @DeleteMapping
    public void deleteAuthor (@PathVariable Long id){
         authorRepository.deleteById(id);
    }
}
