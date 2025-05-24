package com.ssafy.cafe.controller.rest;


import com.ssafy.cafe.model.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Book;
import java.util.List;

@RestController
@RequestMapping("/rest/book")
public class BookRestController {

    @Autowired
    private BookService bookService;

    //책전체 조회

    @GetMapping("")
    @Operation(summary="책전체 조회",
            description = "")
    public ResponseEntity<List<Book>> getBookList(){
        List<Book> list = bookService.getBookList();

        return ResponseEntity.ok(list); 
    }



}
