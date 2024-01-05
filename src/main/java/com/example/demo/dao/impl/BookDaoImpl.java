package com.example.demo.dao.impl;

import com.alibaba.fastjson.JSON;

import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import com.example.demo.entity.BookCover;
import com.example.demo.entity.BookType;
import com.example.demo.repository.BookCoverRepository;
import com.example.demo.repository.BookTypeRepository;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.data.redis.core.RedisTemplate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class BookDaoImpl implements BookDao {

    @Autowired
    private RedisTemplate redisTemplate;

    private final BookRepository bookRepository;

    private final BookCoverRepository bookCoverRepository;
    private final BookTypeRepository bookTypeRepository;

    @Autowired
    public BookDaoImpl(BookRepository bookRepository, BookCoverRepository bookCoverRepository, BookTypeRepository bookTypeRepository) {
        this.bookRepository = bookRepository;
        this.bookCoverRepository = bookCoverRepository;
        this.bookTypeRepository = bookTypeRepository;
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
    public List<Book> findByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContaining(title);
        return books;
    }
    @Override
    public List<Book> findByType(String type) {
        List<BookType> bookTypes = bookTypeRepository.findRelatedBookTypesWithinTwoSteps(type);
        List<Integer> bookIds = new ArrayList<>();
        List<Book> books = new ArrayList<>();
        System.out.println("Searching books of type: " + type);
        BookType oriBookType = bookTypeRepository.findByType(type);
        if (oriBookType != null) {
            List<Integer> ids = oriBookType.getIds();
            for (Integer id : ids) {
                if (!bookIds.contains(id)) {
                    bookIds.add(id);
                    books.add(bookRepository.findById(id).orElse(null));
                }
            }
        }
        for (BookType bookType : bookTypes) {
            List<Integer> ids = bookType.getIds();
            System.out.println(bookType.getType());
            for (Integer id : ids) {
                if (!bookIds.contains(id)) {
                    bookIds.add(id);
                    books.add(bookRepository.findById(id).orElse(null));
                }
            }
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
        BookCover bookCover = bookCoverRepository.findByBookId(id);
        if (bookCover != null) {
            return bookCover.getCover();
        }
        return null;
    }

    @Override
    @Transactional
    public void update(Book updatedBook) {
        System.out.println("Updating book: " + updatedBook.getId() + " in Redis");
        redisTemplate.opsForValue().set("book:" + updatedBook.getId(), JSON.toJSONString(updatedBook));
        redisTemplate.delete("books");
        bookRepository.save(updatedBook);
    }

    @Override
    @Transactional
    public void delete(Book book) {
        System.out.println("Deleting book: " + book.getId() + " from Redis");
        redisTemplate.delete("book:" + book.getId());
        redisTemplate.delete("books");
        bookRepository.delete(book);
    }

    @Override
    @Transactional
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
    @Transactional
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
