package com.ud.bookstore.entity;

public record Book(String bookId,
                   String bookName,
                   String price,
                   String author,
                   String description) {
}
