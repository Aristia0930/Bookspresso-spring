package com.ssafy.cafe.model.service;

import com.ssafy.cafe.model.dao.BookDao;
import com.ssafy.cafe.model.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookDao bookDao;
    @Override
    public List<Book> getBookList() {
        return bookDao.getBookList();
    }
}
