## BookSpresso
<p align="center">
  <img src="/image/0.png" width="80%" alt="메인 이미지 0" />
</p>

---

<p align="center">
  <img src="/image/1.jpg" width="30%" alt="메인 이미지 1" />
  <img src="/image/2.jpg" width="30%" alt="메인 이미지 2" />
  <img src="/image/3.jpg" width="30%" alt="메인 이미지 3" />
</p>

### 1)프로젝트 개

- **프로젝트 명 :** BookSpresso  
- **기간 :** 2025.05.23 ~ 2025.05.27 (5일)  
- **팀 구성 :** 2인 팀 프로젝트 – *김민준(백엔드, 안드로이드), 송정현(안드로이드)*

**북스프레소**는 룸 형태의 북카페에서 사용할 수 있는 모바일 애플리케이션으로,  
사용자가 방 안에서 책을 읽으며 **움직이지 않고 음료 및 디저트를 주문**하고,  
**NFC 태그를 통해 책을 간편하게 대여·반납**할 수 있도록 설계된 스마트 서비스입니다.

---

### 2. 기획 의도 / 배경

기존 북카페에서는 책 대여 및 음식 주문이 번거롭고, 관리 측면에서도 효율이 떨어지는 점이 있었습니다.  
저희는 이를 **디지털화하여 고객 편의성과 운영 효율을 동시에 향상시키는 것**을 목표로 프로젝트를 기획했습니다.

---

### 3. 주요 기술 스택

- **Frontend**: Android (Kotlin)  
- **Backend**: Spring Boot, Spring Security, JWT, OAuth 2.0  
- **Database**: MariaDB  
- **File Storage**: Firebase (이미지 저장)  
- **Deployment**: Cloudtype  
- **결제 연동**: 카카오페이 API  
- **하드웨어 연동**: NFC 태그 인식  

---

### 4. 아키텍처

> (아키텍처 이미지 자리 - 필요 시 아래처럼 이미지 삽입)

<p align="center">
  <img src="/image/4.png" width="80%" alt="아키텍처" />
</p>

---

### 5. DB 테이블 / 트리거

#### 📊 테이블 요약

| 테이블명 | 주요 컬럼 | 설명 | 제약조건 / 키 |
| --- | --- | --- | --- |
| `t_user` | `id`, `name`, `email`, `role`, `provider` | 사용자 정보 | `PK(id)`, `UNIQUE(email)` |
| `t_product` | `id`, `name`, `type`, `price`, `img` | 음료 및 디저트 | `PK(id)` |
| `t_order` | `o_id`, `user_id`, `order_time`, `completed` | 주문 정보 | `PK(o_id)`, `FK(user_id → t_user.id)` |
| `t_order_detail` | `d_id`, `order_id`, `product_id`, `quantity` | 주문 상세 | `PK(d_id)`, `FK(order_id → t_order.o_id)`, `FK(product_id → t_product.id)` |
| `payment` | `id`, `tid`, `user_id`, `order_id`, `item_name`, `total_amount`, `status` | 결제 내역 | `PK(id)`, `UNIQUE(tid)` |
| `t_book` | `isbn`, `title`, `author`, `status`, `img` | 책 정보 | `PK(isbn)` |
| `t_book_rental` | `rental_id`, `user_id`, `isbn`, `rental_date`, `due_date`, `status`, `fee` | 대여 내역 | `PK(rental_id)`, `FK(user_id → t_user.id)`, `FK(isbn → t_book.isbn)` |
| `t_book_recommendation` | `recommendation_id`, `isbn`, `drink_id`, `dessert_id`, `reason` | 책 + 음료 + 디저트 추천 | `PK(recommendation_id)`, `FK(isbn → t_book)`, `FK(drink_id/dessert_id → t_product.id)` |

---

#### 🔁 트리거 요약

| 트리거명 | 시점 | 대상 테이블 | 동작 | 조건 |
| --- | --- | --- | --- | --- |
| `trg_update_book_status_after_rental` | AFTER INSERT | `t_book_rental` | 책 `status = 'borrowed'` 변경 | 항상 실행 |
| `trg_after_return` | AFTER UPDATE | `t_book_rental` | 책 `status = 'available'` 변경 | `NEW.status IN ('returned', 'overdueReturned')` |

---

### 6. 주요 기능
<p align="center">
  <img src="/image/5.jpg" width="30%" alt="주요기능 이미지 1" />
  <img src="/image/6.jpg" width="30%" alt="주요기능 이미지 2" />
  <img src="/image/7.jpg" width="30%" alt="주요기능 이미지 3" />
</p>
- **📚 NFC 기반 책 대여·반납 시스템**  
  스마트폰을 책에 태그하면 자동으로 대여/반납 처리  

- **💳 카카오페이 간편 결제**  
  음료 및 디저트는 카카오페이를 통해 결제  

- **🔐 Spring Security + JWT 인증/인가**  
  JWT 토큰 발급, 사용자 권한 기반 접근 제어  
  관리자 기능도 별도 권한 분리  

- **🔗 OAuth 2.0 로그인 연동 (Google)**  
  이메일 및 SNS 로그인으로 간편 가입  

- **🤖 AI 기반 책 & 음료/디저트 추천 시스템 (GPT API)**  
  - 현재 보유 목록 기반 추천  
  - 추천 이유 포함  
  - Spring 스케줄러로 30분마다 갱신  

- **🛠 관리자 페이지 기능**  
  주문 수락/거절 처리 등 관리 기능

---

### 7. 본인의 역할 및 기여

#### ✅ 백엔드
- REST API 및 DB 설계
- Spring Security + JWT 인증/인가
- Google OAuth 로그인 연동
- NFC 태그 기반 대여/반납 처리
- 카카오페이 결제 연동
- GPT API 연동 및 추천 시스템
- Cloudtype 배포

#### ✅ 프론트엔드
- Lottie 로딩화면 구현
- 관리자/마이페이지 기능 구현
- JWT 기반 자동 로그인 (SharedPreferences 사용)
- OAuth 로그인 연동

---

### 8. 트러블슈팅

#### ❗ 문제 상황

카카오페이 결제 완료 후 앱에서 결과를 처리하지 못하는 문제 발생

#### 🔍 원인 분석

- 외부 브라우저(WebView) 기반 결제
- 결제 후 리다이렉트 URL 필요
- 서버로 자동 결과 전달 안됨

#### 🛠 해결 방법

- 서버 로직 수정 없이  
  **앱에서 리턴된 URL을 WebView로 다시 열어 흐름 마무리**

#### ✅ 결과 및 개선

- 빠른 구현 가능  
- 사용자 UX 흐름은 다소 어색  
- 이후 OAuth 연동 시 Intent 활용으로 개선  
- 리다이렉트 흐름 및 WebView 처리에 대한 깊은 이해 확보

---

### 9. 회고

이번 프로젝트를 통해 안드로이드 개발을 처음으로 경험했습니다.  
다양한 환경에서 번갈아 작업하며 개발 환경 설정의 비효율을 체감했고, 이를 통해 Docker 등 환경 컨테이너 도구의 필요성을 절감하게 되었습니다.  
다음 프로젝트부터는 Docker를 도입해 보다 일관된 개발 환경을 구축할 계획입니다.
