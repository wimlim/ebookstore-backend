package com.example.demo.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.service.BookService;
import com.example.demo.service.KeywordCountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;

import java.util.Map;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;
    @Autowired
    private KeywordCountService keywordCountService;

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
    @GetMapping("/spark")
    public String getSpark() {
        Map<String, Long> map = keywordCountService.countKeywords("D:\\UserData\\Desktop\\ebookstore\\ebookstore-backend\\data");
        // turn into json string
        JSONArray booksJson = new JSONArray();
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            JSONObject bookJson = new JSONObject();
            bookJson.put("keyword", entry.getKey());
            bookJson.put("count", entry.getValue());
            booksJson.add(bookJson);
        }
        return booksJson.toJSONString();
    }
    @GetMapping("/type/{type}")
    public String getBooksByType(@PathVariable String type) {
        return bookService.getBooksByType(type);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateBook(@PathVariable int id, @RequestBody String updatedBook) {
        try {
            bookService.updateBook(id, updatedBook);
            return new ResponseEntity<>("Book updated successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable int id) {
        try {
            bookService.deleteBook(id);
            return new ResponseEntity<>("Book deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody String newBook) {
        try {
            bookService.addBook(newBook);
            return new ResponseEntity<>("Book added successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
