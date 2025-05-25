package com.ssafy.cafe.kakaoPay.controller;

import com.ssafy.cafe.kakaoPay.dto.*;
import com.ssafy.cafe.kakaoPay.service.KakaoPayService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    @PostMapping("/ready")
    @Operation(summary="카카오페이 요청")
    public ResponseEntity<KakaoPayReadyResponse> kakaoPayReady(@RequestBody KakaoReadyRequest requestDto) {
        KakaoPayReadyResponse response = kakaoPayService.readyToPay(requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping ("/success")
    public ResponseEntity<KakaoApproveResponse> afterPayRequest(@RequestParam("pg_token") String pgToken) {

        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);

        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
    }

    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    public ResponseEntity<String> cancel() {

        return ResponseEntity.ok("결제취소");
    }

    /**
     * 결제실패
     */

    @GetMapping("/fail")
    public ResponseEntity<String> fail() {

        return ResponseEntity.ok("결제오류");
    }


    //결제 내역 조회
    @PostMapping("/order")
    public ResponseEntity<Boolean> order(@RequestBody KakaoPaymentOrderRequest request){

        KakaoPaymentOrderResponse orderResponse=kakaoPayService.orderResponse(request);
        if(orderResponse.getStatus()=="SUCCESS_PAYMENT"){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }



    }
}
