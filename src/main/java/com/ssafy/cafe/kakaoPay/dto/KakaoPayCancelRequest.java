package com.ssafy.cafe.kakaoPay.dto;

import lombok.Data;

@Data
public class KakaoPayCancelRequest {
    private String cid;                      // 가맹점 코드
    private String tid;                      // 결제 고유번호
    private Integer cancel_amount;           // 취소 금액
    private Integer cancel_tax_free_amount;  // 취소 비과세 금액
}