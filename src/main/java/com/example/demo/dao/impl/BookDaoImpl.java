package com.example.demo.dao.impl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.RedisTemplate;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class BookDaoImpl implements BookDao {

    @Autowired
    private RedisTemplate redisTemplate;

    private final BookRepository bookRepository;

    @Autowired
    public BookDaoImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        List<Book> books = null;
        System.out.println("Searching all books in Redis");
        Object bs = redisTemplate.opsForValue().get("books");
        if (bs == null) {
            System.out.println("Books not found in Redis");
            System.out.println("Searching all books in DB");
            books = bookRepository.findAll();
            redisTemplate.opsForValue().set("books", JSON.toJSONString(books));
        }
        else {
            System.out.println("Books found in Redis");
            books = JSON.parseArray(bs.toString(), Book.class);
        }
        return books;
    }

    @Override
    public Book findById(int id) {
        Book book = null;
        System.out.println("Searching Book: " + id + " in Redis");
        Object b = redisTemplate.opsForValue().get("book:" + id);
        if (b == null) {
            System.out.println("Book " + id + " not found in Redis");
            System.out.println("Searching Book: " + id + " in DB");
            book = bookRepository.findById(id).orElse(null);
            redisTemplate.opsForValue().set("book:" + id, JSON.toJSONString(book));
        }
        else {
            System.out.println("Book " + id + " found in Redis");
            book = JSON.parseObject(b.toString(), Book.class);
        }

        return book;
    }

    @Override
    public byte[] getCoverData(int id) {
        Book book = null;
        Object b = redisTemplate.opsForValue().get("book:" + id);
        if (b == null) {
            book = bookRepository.findById(id).orElse(null);
            redisTemplate.opsForValue().set("book:" + id, JSON.toJSONString(book));
        }
        else {
            book = JSON.parseObject(b.toString(), Book.class);
        }
        if (book != null) {
            return book.getCover();
        }
        return null;
    }

    @Override
    public void update(Book updatedBook) {
        Book existingBook = findById(updatedBook.getId());

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setLanguage(updatedBook.getLanguage());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setStock(updatedBook.getStock());
        existingBook.setDescription(updatedBook.getDescription());

        redisTemplate.opsForValue().set("book:" + existingBook.getId(), JSON.toJSONString(existingBook));
        redisTemplate.delete("books");
        bookRepository.save(existingBook);
    }

    @Override
    public void delete(Book book) {
        System.out.println("Deleting book: " + book.getId() + " from Redis");
        redisTemplate.delete("book:" + book.getId());
        redisTemplate.delete("books");
        System.out.println("Deleting book: " + book.getId() + " from DB");
        bookRepository.delete(book);
    }

    @Override
    public void updateStock(int bookId, int i) {
        Book book = findById(bookId);
        if (book != null) {
            book.setStock(book.getStock() - i);
            redisTemplate.opsForValue().set("book:" + book.getId(), JSON.toJSONString(book));
            redisTemplate.delete("books");
            bookRepository.save(book);
        }
    }

    @Override
    public void save(Book book) {
        System.out.println("Saving book: " + book.getId() + " to Redis");
        redisTemplate.opsForValue().set("book:" + book.getId(), JSON.toJSONString(book));
        redisTemplate.delete("books");
        bookRepository.save(book);
    }
    @Override
    public int getMaxId() {
        int maxId = 0;
        for (Book book : bookRepository.findAll()) {
            if (book.getId() > maxId) {
                maxId = book.getId();
            }
        }
        return maxId;
    }
}
