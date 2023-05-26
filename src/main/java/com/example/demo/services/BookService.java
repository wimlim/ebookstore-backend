package com.example.demo.services;

import com.example.demo.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BookService {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Book> getAllBooks() {
        String jpql = "SELECT b FROM Book b";
        return entityManager.createQuery(jpql, Book.class).getResultList();
    }

    public Book getBookById(String id) {
        String jpql = "SELECT b FROM Book b WHERE b.id = :id";
        return entityManager.createQuery(jpql, Book.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
