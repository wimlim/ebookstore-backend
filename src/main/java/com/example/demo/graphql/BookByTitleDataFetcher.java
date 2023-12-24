package com.example.demo.graphql;

import com.example.demo.service.BookService;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.example.demo.entity.Book;

import java.util.List;

@Component
public class BookByTitleDataFetcher implements DataFetcher<List<Book>> {

    private final BookService bookService;

    @Autowired
    public BookByTitleDataFetcher(BookService bookService) {
        this.bookService = bookService;
    }
    @Override
    public List<Book> get(DataFetchingEnvironment environment) {
        String title = environment.getArgument("title");
        return bookService.getBooksByTitle(title);
    }
}
