package com.ssafy.cafe.kakaoPay.controller;

import com.ssafy.cafe.kakaoPay.dto.*;
import com.ssafy.cafe.kakaoPay.service.KakaoPayService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.util.Objects;

@Controller
@RequestMapping("/rest/payment")
@RequiredArgsConstructor
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;
    @PostMapping("/ready")
    @ResponseBody
    @Operation(summary="카카오페이 요청")
    public ResponseEntity<KakaoPayReadyResponse> kakaoPayReady(@RequestBody KakaoReadyRequest requestDto) {
        KakaoPayReadyResponse response = kakaoPayService.readyToPay(requestDto);
        return ResponseEntity.ok(response);
    }

//    @GetMapping ("/success")
//    public String afterPayRequest(@RequestParam("pg_token") String pgToken, Model model) {
//
////        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);
//
////        return new ResponseEntity<>(kakaoApprove, HttpStatus.OK);
//        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);
//        model.addAttribute("approve", kakaoApprove);
//        return "redirect:/success.html";  // resources/templates/paymentSuccess.html
//    }

    @GetMapping("/success")
    public String afterPayRequest(@RequestParam("pg_token") String pgToken, HttpServletRequest request) {
        KakaoApproveResponse kakaoApprove = kakaoPayService.approveResponse(pgToken);

        // 결제 결과를 세션이나 쿼리 파라미터로 넘기고 HTML에서 JS로 읽을 수 있도록 할 수 있음
        request.getSession().setAttribute("payResult", kakaoApprove);

        return "redirect:/success.html";  // static 폴더의 success.html 열기
    }

    /**
     * 결제 진행 중 취소
     */
    @GetMapping("/cancel")
    @ResponseBody

    public ResponseEntity<String> cancel() {

        return ResponseEntity.ok("결제취소");
    }

    /**
     * 결제실패
     */

    @GetMapping("/fail")
    @ResponseBody

    public ResponseEntity<String> fail() {

        return ResponseEntity.ok("결제오류");
    }


    //결제 내역 조회
    @PostMapping("/order")
    @ResponseBody

    public ResponseEntity<Boolean> order(@RequestBody KakaoPaymentOrderRequest request){

        KakaoPaymentOrderResponse orderResponse=kakaoPayService.orderResponse(request);
        System.out.println(orderResponse.getStatus());
        if(Objects.equals(orderResponse.getStatus(), "SUCCESS_PAYMENT")){
            return ResponseEntity.ok(true);
        }else{
            return ResponseEntity.ok(false);
        }



    }
}
