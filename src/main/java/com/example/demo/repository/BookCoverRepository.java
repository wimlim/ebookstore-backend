package com.example.demo.repository;

import com.example.demo.entity.BookCover;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCoverRepository extends MongoRepository<BookCover, String> {
    BookCover findByBookId(int bookId);
}