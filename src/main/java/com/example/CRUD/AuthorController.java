package com.example.CRUD;

import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorRepository authorRepository;

    @GetMapping
    public List<Author> getAllAuthors(){
        return authorRepository.findAll();
    }

    //used for filtering data


    //request Param
    @PostMapping
    public Author createAuthor (@RequestBody Author author){
        return authorRepository.save(author);
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
