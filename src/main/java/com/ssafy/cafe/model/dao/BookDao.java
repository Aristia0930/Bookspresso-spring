package com.ssafy.cafe.model.dao;

import com.ssafy.cafe.model.dto.Book;

import java.util.List;

public interface BookDao {

    List<Book> getBookList();
}
