package com.ud.bookstore.repository;

import com.ud.bookstore.entity.Book;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    @Query("{bookId:  '?0'}")
    Optional<Book> findBookByBookId(String bookId);

    @Query(value = "{'bookId' : { $eq : ?0 }}")
    @Update(pipeline = {" { '$set' :  {'bookName' :  ?1 } } "})
    Optional<Book> updateBookNameByBookId(String bookId, String bookName);

    @DeleteQuery
    void deleteBookByBookId(String bookId);
}
