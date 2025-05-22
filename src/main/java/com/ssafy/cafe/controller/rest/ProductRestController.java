package com.ssafy.cafe.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.ProductWithComment;
import com.ssafy.cafe.model.service.ProductService;

@RestController
@RequestMapping("/rest/product")
@CrossOrigin("*")
public class ProductRestController {
	
	@Autowired
	private ProductService productService;
	
	@GetMapping("")
	public ResponseEntity<List<Product>> getProducts(){
		List<Product> products=productService.getProductList();
		
		return ResponseEntity.ok(products);
		
		
		
		
	}
	
	@GetMapping("/{productId}")
	public ProductWithComment selectWithComment(@PathVariable Integer productId) {
		
		ProductWithComment comment=productService.selectWithComment(productId);
		
		
		return comment;
	}
	


    
    
    

	

}