package com.ssafy.cafe.controller.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;


import com.ssafy.cafe.config.auth.PrincipalDetails;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.ssafy.cafe.model.dto.Grade;
import com.ssafy.cafe.model.dto.User;
import com.ssafy.cafe.model.dto.UserInfo;
import com.ssafy.cafe.model.service.UserService;





import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/rest/user")
//@CrossOrigin("*")
@Tag(name = "user controller", description = "사용자 로그인등 사용자 기능을 정의한다.")
public class UserRestController {
    private static final Logger log = LoggerFactory.getLogger(UserRestController.class);
	private List<Level> levels;
	@Autowired
	private UserService service;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "예시 요청",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(
                            name = "기본 예제",
                            value = "{ \"id\": \"3\", \"pass\": \"3\" }"
                    )
            )
    )
	@PostMapping("/login")
	public ResponseEntity<User> login(@RequestBody User user,HttpServletResponse response){


		System.out.println(user);
		User info=service.login(user.getId(), user.getPass());
	    if (info == null) {
	        return ResponseEntity.badRequest().build();
	    } else {
	        Cookie cookie = new Cookie("ssafy_id", "로그인처리");
	        cookie.setMaxAge(60 * 60 * 24 * 30); // 쿠키 30일 동안 유지
	        cookie.setPath("/"); // 하위 모든 경로에 쿠키 포함
	        response.addCookie(cookie);
	    }
		System.out.println(info);
		return ResponseEntity.ok(info);

	}


    // 유저정보 린터하기

//    @PreAuthorize("hasRole('USER')")
//    @PostMapping("/info")
//    public ResponseEntity<User> userInfo(@AuthenticationPrincipal PrincipalDetails userDetails) {
//        String userId = userDetails.getUsername();  // 또는 userDetails.getUser().getId()
//
//        User info=service.selectUser(userId);
//
//        log.debug(userId);
//        log.debug(String.valueOf(info));
//
//        if (info == null) {
//            return ResponseEntity.badRequest().build();
//        }
//        System.out.println(info);
//        return ResponseEntity.ok(info);
//    }

    @PostMapping("/info")
    @Operation(summary="유저 정보 리턴")
    public ResponseEntity<User> userInfo(@RequestBody User user) {

        User info=service.selectUser(user.getId());



        if (info == null) {
            return ResponseEntity.badRequest().build();
        }
        System.out.println(info);
        return ResponseEntity.ok(info);
    }

    //토큰 유효한지만 확인하기
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/check")
    @Operation(summary="토큰이 유효한지 체크")
    public ResponseEntity<Boolean> checkJwt() {

        return ResponseEntity.ok(true);
    }

//	@PostMapping("/info")
//	public ResponseEntity<UserInfo> userInfo(@RequestBody User user){
//		UserInfo info=service.userInfo(user.getId());
//		System.out.println(info.toString());
//		//여기서 등급분류하자.
//
//		 Map<String, Object> grade =getGrade(info.getUser().getStamps());
//		 System.out.println(getGrade(225));
//		info.setGrade(new Grade((String) grade.get("img"),(Integer) grade.get("step"), (Integer) grade.get("stepMax"), (Integer) grade.get("to"), (String) grade.get("title")));
//		return ResponseEntity.ok(info);
//	}
//

    
    @PostMapping("")
    @Operation(summary="회원가입")
    public boolean join(@RequestBody User user) {
        System.out.printf("회원가입");
        String encodedPassword = passwordEncoder.encode(user.getPass());
        user.setPass(encodedPassword);
        user.setRole("ROLE_USER");
    	int result = service.join(user);
    	if (result == 1 ) {
    		System.out.println();
    		return true;
    	} else {
    		return false;
    	}
    	
    }
    
    @GetMapping("/isUsed")
    public boolean isUsed(String id) {
    	return service.isUsedId(id);
    }
    

    /**
     * 사용자 등급정보를 계산하여 리턴한다.
     * 이미지가 seeds.png, 등급이 씨앗, step이 1, stepMax가 1, to가 7 이라면,
     * 씨앗 1단계 등급의 사용자가 다음단계로 가려면 7개 더 모아야 함을 의미한다.
     *  img, step, stepMax, to, title이 각 key가 되고, 
     *  각각의 key을 맞게 입력하여 리턴하면 된다.
     *   
     *  src/test/java에 있는 테스트 케이스를 통과하면 정상동작한다. 
     *   
     * @param stamp
     * @return
     */
    class Level {
        private String title;
        private int unit;
        private int max;
        private String img;

        private Level(String title, int unit, int max, String img) {
            this.title = title;
            this.unit = unit;
            this.max = max;
            this.img = img;
        }
    }

 


        public void setup() {
            levels = new ArrayList<>();
            levels.add(new Level("씨앗", 10, 50, "seeds.png"));
            levels.add(new Level("꽃", 15, 125, "flower.png"));
            levels.add(new Level("열매", 20, 225, "coffee_fruit.png"));
            levels.add(new Level("커피콩", 25, 350, "coffee_beans.png"));
            levels.add(new Level("커피나무", Integer.MAX_VALUE, Integer.MAX_VALUE, "coffee_tree.png"));
        }

        public Map<String, Object> getGrade(Integer stamp) {
            setup();
            Map<String, Object> grade = new HashMap<>();

            int accumulated = 0; // 누적 스탬프 개수
            for (int i = 0; i < levels.size(); i++) {
                Level level = levels.get(i);

                if (stamp < accumulated + level.max) {
                    int step;
                    int toNext;

                    // 초기 레벨(씨앗 0단계) 특별 처리
                    if (stamp == 0) {
                        step = 0;
                        toNext = 1;
                    } else {
                        step = (stamp - accumulated) / level.unit;
                        toNext = level.unit - ((stamp - accumulated) % level.unit);
                        step += 1;
                    }

                    int stepMax = level.max / level.unit;
                    step = Math.min(step, stepMax);

                    // 최종 단계에 도달했을 때 처리
                    if (step == stepMax) {
                        toNext = 0;
                    }

                    // 레벨의 마지막 스탬프일 때 특별 처리
                    if (stamp == accumulated + level.max - 1) {
                        step = stepMax;
                        toNext = 1;
                    }

                    grade.put("title", level.title);
                    grade.put("step", step);
                    grade.put("stepMax", level.unit);
                    grade.put("to", toNext);
                    grade.put("img", level.img);
                    return grade;
                }

                accumulated += level.max;
            }

            // 커피나무(최종 단계)
            Level lastLevel = levels.get(levels.size() - 1);
            grade.put("title", lastLevel.title);
            grade.put("step", 0);
            grade.put("stepMax", 0);
            grade.put("to", 0);
            grade.put("img", lastLevel.img);
            return grade;
        }





    

}
