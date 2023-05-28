package com.example.demo.repository;

import com.example.demo.entity.Book;
import com.example.demo.entity.CartItem;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByUserAndBook(User user, Book book);

    List<CartItem> findByUser(User user);

    @Transactional
    void deleteByUser(User user);
}
