package com.ud.bookstore.controller;

import com.ud.bookstore.model.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController()
@RequestMapping("/book-store")
public class BookController {

    @GetMapping("/{bookId}")
    public ResponseEntity<String> getBook(@PathVariable String bookId) {
        return new ResponseEntity<>("Book found " + bookId, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<BookDTO>> getAllBooks() {

        List<BookDTO> bookList = Arrays.asList(
                BookDTO.builder()
                        .bookId("1")
                        .bookName("Java Essentials")
                        .bookAuthor("Udith")
                        .build(),
                BookDTO.builder()
                        .bookId("")
                        .bookName("MongoDB 101")
                        .bookAuthor("Nuwan")
                        .build(),
                BookDTO.builder()
                        .bookId("")
                        .bookName("Python for Dummies")
                        .bookAuthor("DV")
                        .build());

        return ResponseEntity.ok(bookList);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody() BookDTO book) {

        return ResponseEntity.ok("Book added: " + book.getBookId());
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateBook(String book) {

        return new ResponseEntity<>("Book updated", HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId) {

        return new ResponseEntity<>("Book deleted", HttpStatus.OK);
    }
}
