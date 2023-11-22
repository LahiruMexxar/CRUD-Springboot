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
    public ResponseEntity<ApiResponse<List<Author>>> addManyAuthors(@RequestBody List<Author> authors) {
        try {
            List<Author> savedAuthors = new ArrayList<>();

            for (Author author : authors) {
                // Check if an author with the same name already exists
                Optional<Author> existingAuthor = authorRepository.findByName(author.getName());
                if (existingAuthor.isPresent()) {
                    throw new DuplicateKeyException("Author with the same name already exists.");
                }

                // Save the author
                Author savedAuthor = authorRepository.save(author);
                savedAuthors.add(savedAuthor);
            }

            ApiResponse<List<Author>> apiResponse = new ApiResponse<>(201, "Authors added successfully", savedAuthors);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            ApiResponse<List<Author>> errorResponse = new ApiResponse<>(400, e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            ApiResponse<List<Author>> errorResponse = new ApiResponse<>(500, "An error occurred while adding the authors", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updateauthor/{authorId}")
    public ResponseEntity<ApiResponse<Author>> updateAuthor(@PathVariable Long authorId, @RequestBody Author updatedAuthor) {
        try {
            // Check if the author with the given ID exists
            Optional<Author> existingAuthorOptional = authorRepository.findById(authorId);
            if (existingAuthorOptional.isEmpty()) {
                throw new NoSuchElementException("Author not found.");
            }

            // Get the existing author from the database
            Author existingAuthor = existingAuthorOptional.get();

            // Update the existing author with the values from the updated author
            existingAuthor.setName(updatedAuthor.getName());
            existingAuthor.setNationality(updatedAuthor.getNationality());

            // Save the updated author
            Author savedAuthor = authorRepository.save(existingAuthor);

            ApiResponse<Author> apiResponse = new ApiResponse<>(200, "Author updated successfully", savedAuthor);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            ApiResponse<Author> errorResponse = new ApiResponse<>(404, e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            ApiResponse<Author> errorResponse = new ApiResponse<>(500, "An error occurred while updating the author", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteauthor")
    public ResponseEntity<ApiResponse<Void>> deleteAuthors(@RequestBody List<Long> authorIds) {
        try {
            for (Long authorId : authorIds) {
                // Check if the author exists in the database
                Optional<Author> existingAuthorOptional = authorRepository.findById(authorId);
                if (existingAuthorOptional.isPresent()) {
                    // If the author exists, delete it
                    authorRepository.deleteById(authorId);
                }
            }

            ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Authors deleted successfully", null);
            return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            ApiResponse<Void> errorResponse = new ApiResponse<>(500, "An error occurred while deleting the authors", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteAuthor/{authorId}")
    public ResponseEntity<ApiResponse<Void>> deleteAuthor(@PathVariable Long authorId) {
        try {
            // Check if the author with the given ID exists
            Optional<Author> existingAuthorOptional = authorRepository.findById(authorId);
            if (existingAuthorOptional.isPresent()) {
                Author author = existingAuthorOptional.get();

                // Delete all books associated with the author
                bookRepository.deleteByAuthor(author);

                // Then, delete the author
                authorRepository.deleteById(authorId);

                ApiResponse<Void> apiResponse = new ApiResponse<>(204, "Author and associated books deleted successfully", null);
                return new ResponseEntity<>(apiResponse, HttpStatus.NO_CONTENT);
            } else {
                // If the author does not exist, return a 404 Not Found response
                ApiResponse<Void> errorResponse = new ApiResponse<>(404, "Author not found", null);
                return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            ApiResponse<Void> errorResponse = new ApiResponse<>(500, "An error occurred while deleting the author", null);
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}


