# 당신을 위한 팝업스토어 "Popcorn"
|![메인화면](https://github.com/user-attachments/assets/4117c5ea-0969-4c58-83b4-d432f8e5bd33)|![전체보기](https://github.com/user-attachments/assets/6622b77f-222f-441e-b76f-cc16b5c4a1c8)|  
|---|---|  
|메인화면|전체보기|
- 오늘의 추천 팝업, 찜 목록, 사용자 관심사, 마감 예정 팝업들을 보여줍니다.
- 전체보기를 눌러 해당 목록의 팝업스토어만 확인할 수 있습니다.
## 메인화면 - 상세보기 및 리뷰 쓰기
|![정보탭](https://github.com/user-attachments/assets/c29e1910-2460-42db-97ed-1ed31d3554eb)|![후기탭](https://github.com/user-attachments/assets/ca751455-d9ad-425a-be47-ffaa923bd8ac)|![리뷰쓰기](https://github.com/user-attachments/assets/94d2c476-cf69-4441-8dd5-ae3e68fabef2)|  
|---|---|---|  
|상세화면 - 정보|상세화면 - 후기|리뷰 쓰기|  
- 공유 및 찜 기능을 활용할 수 있습니다.
- 예약하기 버튼을 눌러 해당 팝업의 예약 사이트로 이동합니다.
- 리뷰 작성 시 만족도를 0.5점 단위로 설정할 수 있습니다.
- 최대 10장의 사진을 등록할 수 있습니다.
### "관심사 기반, 위치 기반 팝업스토어 추천 플랫폼"  
- 관심사를 선택하고, 이에 맞는 팝업스토어를 추천 받을 수 있어요!
- 위치 기반 검색을 통해, 내 주위의 팝업스토어를 찾을 수 있어요!

## 📚 문서
| 팀 노션 | 디자인 | 개발 기록 | 학습 기록 | 회의록 | 
|---|--- | --- | --- | --- |
| 🏠 [팀 노션](https://branch-cheque-736.notion.site/GDSC-Popcorn-10ab725a066580ea8373ca9d109e261a?pvs=4) | 🎨 [디자인](https://www.figma.com/design/DVerEwsns12Js5WtTijImy/popcorn?node-id=241-931&node-type=canvas&m=dev) | ✏️ [개발 기록](https://branch-cheque-736.notion.site/117b725a0665802ab262cb1ad21a2da3?pvs=4)| ✏️ [학습 기록](https://branch-cheque-736.notion.site/10db725a06658055ba23dcd0e67209a8?pvs=4)|📝 [회의록](https://branch-cheque-736.notion.site/181530912f794e299f71ceb453279fc5?pvs=4)


# 목차
### [1. 프로젝트 소개](https://github.com/GDSC-Popcorn/Popcorn-BE/new/develop?filename=README.md#프로젝트-소개)
### [2. 개발 과정 중 문제](https://github.com/GDSC-Popcorn/Popcorn-BE/new/develop?filename=README.md#개발-과정-중-문제)

# 1. 프로젝트 소개
팝업 스토어의 정보를 얻기 위해 SNS를 주로 이용합니다.  

그러나 SNS에서 제공되는 정보는 제한적인 정보 뿐 입니다.

'내 근처의, 취향에 맞는 팝업 스토어를 한 눈에 볼 수 없을까?'라는 생각에서 팝콘 앱이 시작되었습니다.  

또한 실시간 채팅을 이용해 팝업스토어의 현장 상황, 다양한 기타 정보들을 공유하여 사용자들이 소중한 시간을 지킬 수 있습니다!

## 기술 스택 
<img width="1000" height="647" alt="팝콘_구조도" src="https://github.com/user-attachments/assets/ffb4ee67-9161-4357-ad87-012fc05207fe" />
Java17, Spring Boot, Spring Security, Spring Data JPA, Mysql, Redis


# 2. ✏️ 개발 과정 중 문제
### 회원가입 및 로그인
  
  소셜 로그인을 oidc기반 idtoken발급으로 처리했습니다. idToken을 가지고 소셜로그인을 하면 서버에서 발급해주는 jwt가 발급 되게 했습니다.
  로그인 필터와 jwt필터를 구현하여 시큐리티로 로그인 처리를 하였습니다. 
  
### 리뷰 좋아요 동시성 처리
  
  리뷰 좋아요 기능이 테스트 후 동시성 처리가 안된걸 파악
  
  -> 
  비관적 락, 낙관적 락 중 비관적 락인 리뷰 조회 시에 X-Lock을 설정해서 Lock 설정(공유 자원 자체에 lock을 거는 행위)하여 동시성 처리를 하였습니다. 
  낙관적 락, 비관적 락 중에서 낙관적 락을 선택하지 않은 이유는 충돌이 많이 발생할 것이라고 예상했기 때문입니다.
  충돌이 많이 발생할 때 낙관적 락을 사용하면 그만큼 재시도가 많이 발생하여(버전이 안 맞으면 재시도가될것임 ) 성능이 좋지 않을 것이기 때문에 비관적 락을 고려했습니다.
  
### 홈화면에서의 SQL 쿼리문 튜닝을 통한 성능 개선 시도
  
  홈화면에서 다수의 페이징 처리된 팝업 리스트를 받아와야했었는데, 기존에는 불필요하게 모든 엔티티를 다 들고 와서 10만건 더미 데이터 넣은 후  JMeter를 사용한 500명의 동시 요청 시에 OutOfMemoryError 에러 발생 
  
  ->
  원래 코드로는 10만건 데이터는 못 버틴다고 판단해서 데이터를 1만건으로 줄이고 dto projection으로 필요한 컬럼들만 들고오도록 수정
  평균 latency 68.45% 개선


### 하나의 팝업에 대한 리뷰를 조회에서 N+1문제
리뷰를 조회하면서 글을 작성한 유저의 닉네임, 리뷰이미지 리스트를 불러올 때 N+1문제가 발생

->
쿼리 한번에 유저랑 이미지들을 들고오게 변경 [Fetch Join으로 N+1문제를 다뤄보자](https://blog.naver.com/wjdals980424/223892786347)
