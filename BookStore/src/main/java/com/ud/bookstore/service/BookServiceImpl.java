package com.ud.bookstore.service;

import com.ud.bookstore.entity.Book;
import com.ud.bookstore.mapper.BookMapper;
import com.ud.bookstore.model.BookDTO;
import com.ud.bookstore.repository.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository repository) {
        this.bookRepository = repository;
    }

    @Override
    public BookDTO getBook(String bookId) {

        Optional<Book> book = bookRepository.findBookByBookId(bookId);
        return book.map(BookMapper::toDto).orElse(null);
    }

    @Override
    public List<BookDTO> getAllBooks() {

        List<Book> bookList = bookRepository.findAll();
        return bookList.stream()
                .map(BookMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {

        Book newBook = bookRepository.insert(BookMapper.toEntity(bookDTO));
        return BookMapper.toDto(newBook);
    }

    @Override
    public void updateBook(BookDTO bookDTO) {

        Optional<Integer> updatedCount = bookRepository.updateBookNameByBookId(bookDTO.getBookId(), bookDTO.getBookName());

    }

    @Override
    public void deleteBook(String bookId) {
        bookRepository.deleteBookByBookId(bookId);

    }
}
