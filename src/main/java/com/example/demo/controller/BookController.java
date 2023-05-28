package com.example.demo.controller;

import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable int id) {
        return bookService.getBookById(id);
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getBookCover(@PathVariable("id") int id) {
        byte[] coverData = bookService.getBookCoverData(id);

        if (coverData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(coverData.length);
            return new ResponseEntity<>(coverData, headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable int id, @RequestBody String updatedBook) {
        try {
            // 在这里你可以解析 updatedBook 字符串，根据需要进行处理
            bookService.updateBook(id, updatedBook);
            return new ResponseEntity<>("Book updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
