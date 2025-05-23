package com.ssafy.cafe.controller.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/manager")

@Tag(name = "manager controller", description = "관리자 기능을 정의한다.")

public class ManagerRestController {

    //음식 주문 목록 물러오기
    //이때 상태값이 ㄴ
}
