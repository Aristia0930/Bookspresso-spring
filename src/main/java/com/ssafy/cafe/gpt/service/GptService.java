package com.ssafy.cafe.gpt.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.cafe.gpt.GptConfig;
import com.ssafy.cafe.gpt.dto.GptRequest;
import com.ssafy.cafe.gpt.dto.GptResponse;
import com.ssafy.cafe.gpt.dto.RecommendationResponse;
import com.ssafy.cafe.model.dao.BookDao;
import com.ssafy.cafe.model.dao.ProductDao;
import com.ssafy.cafe.model.dao.RecommendationDao;
import com.ssafy.cafe.model.dto.Book;
import com.ssafy.cafe.model.dto.BookRecommendation;
import com.ssafy.cafe.model.dto.Product;
import com.ssafy.cafe.model.dto.ProductWithComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
public class GptService {

    private final String secretKey;
    private final WebClient webClient;
    @Autowired
    private GptConfig gptConfig;
    @Value("${gpt.model}")
    private String model;

    @Autowired
    private ProductDao productDao;
    @Autowired
    private BookDao bookDao;
    @Autowired
    private RecommendationDao recommendationDao;

    public GptService(@Value("${gpt.key}") String secretKey) {
        this.secretKey = secretKey;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
                .build();
    }

    public RecommendationResponse gptApiRequest() throws JsonProcessingException {
        String text="You are a helpful assistant that recommends one book along with a suitable drink and dessert based on the given lists.\n" +
                "\n" +
                "Available Books (each has: isbn, title, author, summary, status, img):\n" +gptConfig.getBookList() +
                "\n" +
                "Available Products (each has: id, name, type, price, img; type is 'drink' or 'dessert'):\n" +
                gptConfig.getProducts()+
                "Please recommend one book and one drink and one dessert that best match the book. \n" +
                "\n" +
                "Return the response in this exact JSON format only:\n" +
                "\n" +
                "{\n" +
                "  \"book\": {\n" +
                "    \"isbn\": \"<isbn>\",\n" +
                "    \"title\": \"<title>\",\n" +
                "    \"author\": \"<author>\",\n" +
                "    \"summary\": \"<summary>\",\n" +
                "    \"status\": \"<status>\",\n" +
                "    \"img\": \"<img>\"\n" +
                "  },\n" +
                "  \"drink\": {\n" +
                "    \"id\": <id>,\n" +
                "    \"name\": \"<name>\",\n" +
                "    \"type\": \"drink\",\n" +
                "    \"price\": <price>,\n" +
                "    \"img\": \"<img>\"\n" +
                "  },\n" +
                "  \"dessert\": {\n" +
                "    \"id\": <id>,\n" +
                "    \"name\": \"<name>\",\n" +
                "    \"type\": \"dessert\",\n" +
                "    \"price\": <price>,\n" +
                "    \"img\": \"<img>\"\n" +
                "  }\n" +
                "}";
        GptRequest request=new GptRequest();
        request.setModel(model);
        request.setInput(text);
        request.setTemperature(1.2);
        GptResponse rs=webClient.post()
                .uri("v1/responses")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(GptResponse.class)
                .block();

        System.out.println(rs.getOutput());

        String jsonString =rs.getOutput().get(0).getContent().get(0).getText();


        ObjectMapper mapper = new ObjectMapper();
        RecommendationResponse response = mapper.readValue(jsonString, RecommendationResponse.class);

        return response;
    }

    //db 에 넣기
    public int gptApiInstert(RecommendationResponse response){
        BookRecommendation bookRecommendation=new BookRecommendation();

        bookRecommendation.setRecommendDate(LocalDateTime.now());
        bookRecommendation.setIsbn(response.getBook().getIsbn());
        bookRecommendation.setDrinkId(response.getDrink().getId());
        bookRecommendation.setDessertId(response.getDessert().getId());
        int rs=recommendationDao.insert(bookRecommendation);
        return rs;
    }


    //스케줄러 조회 실행
    public int addGptApi() throws JsonProcessingException {
        RecommendationResponse response= gptApiRequest();
        return gptApiInstert(response);
    }

    //조회
    public RecommendationResponse getApiRecommendation(){
        BookRecommendation bookRecommendation=recommendationDao.select();

        //책isbn
        String isbn=bookRecommendation.getIsbn();
        Book book =bookDao.getBook(isbn);
        int drinkId=bookRecommendation.getDrinkId();
        ProductWithComment productw1=productDao.selectWithInfo(drinkId);
        int dessertId=bookRecommendation.getDessertId();
        ProductWithComment productw2=productDao.selectWithInfo(dessertId);

        RecommendationResponse response=new RecommendationResponse();

        Product product1=new Product();
        product1.setId(productw1.getId());
        product1.setImg(productw1.getImg());
        product1.setName(productw1.getName());
        product1.setPrice(productw1.getPrice());
        product1.setType(productw1.getType());

        Product product2=new Product();
        product2.setId(productw2.getId());
        product2.setImg(productw2.getImg());
        product2.setName(productw2.getName());
        product2.setPrice(productw2.getPrice());
        product2.setType(productw2.getType());

        response.setBook(book);
        response.setDrink(product1);
        response.setDessert(product2);

        return response;
    }



}
