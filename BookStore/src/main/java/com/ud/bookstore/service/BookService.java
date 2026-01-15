package com.ud.bookstore.service;

import com.ud.bookstore.model.BookDTO;

import java.util.List;

public interface BookService {

    public BookDTO getBook(String bookId);

    public List<BookDTO> getAllBooks();

    public BookDTO addBook(BookDTO bookDTO);

    public void updateBook(BookDTO bookDTO);

    public void deleteBook(String BookId);
}
