package com.ssafy.cafe.kakaoPay.service;

import com.ssafy.cafe.kakaoPay.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service

public class KakaoPayService {
    private final String secretKey;
    private KakaoPayReadyResponse kakaoReady;
    private KakaoReadyRequest latestReadyRequest;
    @Value("${kakaopay.cid}")
    private String cid;
    private final WebClient webClient;

    public KakaoPayService(@Value("${kakaopay.secretKey}") String secretKey) {
        this.secretKey = secretKey;
        this.webClient = WebClient.builder()
                .baseUrl("https://open-api.kakaopay.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "SECRET_KEY " + secretKey)
                .build();
    }


    public KakaoPayReadyResponse readyToPay(KakaoReadyRequest requestDto) {
        requestDto.setCid(cid);
//        requestDto.setApproval_url("http://localhost:9987/rest/payment/success");
//        requestDto.setCancel_url("http://localhost:9987/rest/payment/cancel");
//        requestDto.setFail_url("http://localhost:9987/rest/payment/fail");
        requestDto.setApproval_url("https://port-0-bookcafe-m5al105w4c7a25c2.sel4.cloudtype.app/rest/payment/success");
        requestDto.setCancel_url("https://port-0-bookcafe-m5al105w4c7a25c2.sel4.cloudtype.app/rest/payment/cancel");
        requestDto.setFail_url("https://port-0-bookcafe-m5al105w4c7a25c2.sel4.cloudtype.app/rest/payment/fail");


        kakaoReady=webClient.post()
                .uri("/online/v1/payment/ready")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(KakaoPayReadyResponse.class)
                .block(); // 동기 방식으로 호출
        latestReadyRequest = requestDto;

        return kakaoReady;
    }

    public KakaoApproveResponse approveResponse (String pgToken){
        KakaoApproveRequest requestDto= new KakaoApproveRequest();
        requestDto.setCid(cid);
        requestDto.setTid(kakaoReady.getTid());
        requestDto.setPartner_order_id(latestReadyRequest.getPartner_order_id());
        requestDto.setPartner_user_id(latestReadyRequest.getPartner_user_id());
        requestDto.setPg_token(pgToken);

        return webClient.post()
                .uri("/online/v1/payment/approve")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(KakaoApproveResponse.class)
                .block();

    }

    public KakaoPaymentOrderResponse orderResponse (KakaoPaymentOrderRequest request){
        KakaoApproveRequest requestDto= new KakaoApproveRequest();
        requestDto.setCid(cid);
        requestDto.setTid(request.getTid());

        return webClient.post()
                .uri("/online/v1/payment/order")
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(KakaoPaymentOrderResponse.class)
                .block();

    }


}
