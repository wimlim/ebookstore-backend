package com.example.demo.controller;

import com.example.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public String getBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public String getBook(@PathVariable String id) {
        return bookService.getBookById(id);
    }
    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getBookCover(@PathVariable("id") String id) {
        byte[] coverData = bookService.getBookCoverData(id);

        if (coverData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(coverData.length);
            return new ResponseEntity<>(coverData, headers, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
