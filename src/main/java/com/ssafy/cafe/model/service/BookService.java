package com.ssafy.cafe.model.service;

import com.ssafy.cafe.model.dto.Book;

import java.util.List;

public interface BookService {



    List<Book> getBookList();

    Book getBook(String isbn);
}
