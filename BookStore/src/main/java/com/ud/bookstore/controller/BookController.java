package com.ud.bookstore.controller;

import com.ud.bookstore.model.BookDTO;
import com.ud.bookstore.service.BookService;
import jakarta.validation.Valid;
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

import java.util.List;

@RestController()
@RequestMapping("/book-store")
public class BookController {

    private final BookService bookService;

    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookDTO> getBook(@PathVariable String bookId) {
        BookDTO bookDto = bookService.getBook(bookId);
        return new ResponseEntity<>(bookDto, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<BookDTO>> getAllBooks() {

        /*List<BookDTO> bookList = Arrays.asList(
                BookDTO.builder()
                        .bookId("1")
                        .bookName("Java Essentials")
                        .bookAuthor("Udith")
                        .build(),
                BookDTO.builder()
                        .bookId("2")
                        .bookName("MongoDB 101")
                        .bookAuthor("Nuwan")
                        .build(),
                BookDTO.builder()
                        .bookId("3")
                        .bookName("Python for Dummies")
                        .bookAuthor("DV")
                        .build());*/

        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBook(@RequestBody() @Valid BookDTO book) {

        BookDTO bookDTO = bookService.addBook(book);
        return ResponseEntity.ok("New Book added with BookID: " +bookDTO.getBookId());
    }

    @PutMapping("/update")
    public ResponseEntity<BookDTO> updateBook(@RequestBody() @Valid BookDTO book) {
        BookDTO bookDto = bookService.updateBook(book);
        return ResponseEntity.ok(bookDto);
    }

    @DeleteMapping("/delete/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId) {
        bookService.deleteBook(bookId);
        return new ResponseEntity<>("Book deleted", HttpStatus.OK);
    }
}
