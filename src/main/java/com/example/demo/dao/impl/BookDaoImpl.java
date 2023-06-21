package com.example.demo.dao.impl;

import com.example.demo.dao.BookDao;
import com.example.demo.entity.Book;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class BookDaoImpl implements BookDao {

    private final BookRepository bookRepository;

    @Autowired
    public BookDaoImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(int id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public byte[] getCoverData(int id) {
        Book book = bookRepository.findById(id).orElse(null);

        if (book != null) {
            return book.getCover();
        }
        return null;
    }

    @Override
    public void update(Book updatedBook) {
        Book existingBook = bookRepository.findById(updatedBook.getId()).orElse(null);

        if (existingBook == null) {
            throw new IllegalArgumentException("Book not found");
        }

        existingBook.setTitle(updatedBook.getTitle());
        existingBook.setAuthor(updatedBook.getAuthor());
        existingBook.setLanguage(updatedBook.getLanguage());
        existingBook.setIsbn(updatedBook.getIsbn());
        existingBook.setPrice(updatedBook.getPrice());
        existingBook.setStock(updatedBook.getStock());
        existingBook.setDescription(updatedBook.getDescription());

        bookRepository.save(existingBook);
    }

    @Override
    public void delete(Book book) {
        bookRepository.delete(book);
    }

    @Override
    public void updateStock(int bookId, int i) {
        Book book = bookRepository.findById(bookId).orElse(null);
        if (book != null) {
            book.setStock(book.getStock() - i);
            bookRepository.save(book);
        }
    }

    @Override
    public void save(Book book) {
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
