package com.ssafy.cafe.kakaoPay.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KakaoPayCancelResponse {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;

    private Amount amount;
    private ApprovedCancelAmount approved_cancel_amount;
    private CanceledAmount canceled_amount;
    private CancelAvailableAmount cancel_available_amount;

    private String item_name;
    private String item_code;
    private Integer quantity;

    private LocalDateTime created_at;
    private LocalDateTime approved_at;
    private LocalDateTime canceled_at;

    private String payload;

    @Data
    public class Amount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }
    @Data
    public class ApprovedCancelAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @Data
    public class CanceledAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }

    @Data
    public class CancelAvailableAmount {
        private Integer total;
        private Integer tax_free;
        private Integer vat;
        private Integer point;
        private Integer discount;
        private Integer green_deposit;
    }
}