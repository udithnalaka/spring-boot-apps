package com.ud.bookstore.mapper;

import com.ud.bookstore.entity.Book;
import com.ud.bookstore.model.BookDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BookMapper {

    public static BookDTO toDto(Book book) {

        log.info("mapping Book entity to DTO");
        return BookDTO.builder()
                .bookId(book.bookId())
                .bookName(book.bookName())
                .bookAuthor(book.author())
                .price(book.price())
                .description(book.description())
                .build();
    }

    public static Book toEntity(BookDTO bookDTO) {
        return new Book(bookDTO.getBookId(), bookDTO.getBookName(),
                bookDTO.getPrice(), bookDTO.getBookAuthor(), bookDTO.getDescription());
    }
}

