package com.matheus.junit_rest_api.controller;

import com.matheus.junit_rest_api.model.Book;
import com.matheus.junit_rest_api.repository.BookRepository;
import com.sun.source.tree.LambdaExpressionTree;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    public ResponseEntity<List<Book>> getALlBookRecords() {

        return ResponseEntity.ok().body(bookRepository.findAll());
    }

    @GetMapping("{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable("bookId") Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok().body(optionalBook.get());
    }

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody @Valid Book bookRecord) {
        return ResponseEntity.ok().body(bookRepository.save(bookRecord));
    }

    @PutMapping
    public ResponseEntity<Book> updateBookRecord(@RequestBody @Valid Book bookRecord) {
        Optional<Book> optionalBook = bookRepository.findById(bookRecord.getId());
        if (optionalBook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Book existingBook = optionalBook.get();
        existingBook.setName(bookRecord.getName());
        existingBook.setSummary(bookRecord.getSummary());
        existingBook.setRating(bookRecord.getRating());
        return ResponseEntity.ok().body(bookRepository.save(existingBook));

    }

    @DeleteMapping("{bookId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Void> deleteBookRecord(@PathVariable("bookId") Long bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        };
        bookRepository.deleteById(optionalBook.get().getId());
        return ResponseEntity.ok().build();
    }

}
