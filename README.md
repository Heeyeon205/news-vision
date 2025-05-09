# 📙 쉽게 보는 지식 플랫폼 NEWSION
<p align="center"> <b>2025.04.15 ~ 2025.05.08</b> </p>
<img width="1535" alt="newsion-title2" src="https://github.com/user-attachments/assets/af28d22c-bf2b-48b3-9d6a-1530e0429ba5" />
URL: https://newsion.kro.kr
<br>

### Test Account
|ID|PW|ROLE|Remarks|
|--|--|--|--|
|admin|admin|ROLE_ADMIN|모든 권한|
|creator|1234|ROLE_CREATOR|뉴스, 커뮤니티 작성|
|user|1234|ROLE_USER|커뮤니티 작성|
<br/>

## Overview

**뉴스는 넘쳐나고, 커뮤니티는 시끄럽고, 지식은 어렵습니다.**  
뉴션은 쉽고, 재미있게 세상의 지식을 연결하는 플랫폼입니다.  
뉴션과 함께 나만의 세상을 확장해보세요.

<br/>

- 뉴션(NEWSION)은 읽기 어려운 뉴스를 공식 계정 및 크리에이터들이 쉽고 명확하게 풀어주는 뉴스 플랫폼입니다.
- 바쁜 현대인을 위해 하루 10개의 주요 뉴스를 3줄 요약으로 핵심 정보를 빠르게 제공합니다.
- 커뮤니티 기능을 통해 뉴스에 대한 지적인 소통과 다양한 경험을 자유롭게 공유할 수 있습니다.
- 뉴스 정보와 사회 이슈에 대해 사용자 투표를 진행할 수 있는 참여형 콘텐츠를 제공합니다.
<br/>

## Members
|PL|BE|BE|BE|
|:--:|:--:|:--:|:--:|
|<img src="https://github.com/user-attachments/assets/3d7136a6-b120-4124-8aea-82d0cc91c21f" width="200" heigth="300"/>|<img src="https://github.com/user-attachments/assets/5efcb405-a6ce-4c29-aba4-f48e940296d7" width="200" heigth="300"/>|<img src="https://github.com/user-attachments/assets/ca0d6ba4-7a50-477f-a984-4816121ea2e5" width="200" heigth="300"/>|<img src="https://github.com/user-attachments/assets/096a54a2-f2c9-47e6-b418-530b2eca7c19" width="200" heigth="300"/>|
|[이종현](https://github.com/Heeyeon205)|[구승회](https://github.com/shoot3957)|[송명보](https://github.com/Songmyungbo)|[정지원](https://github.com/jiwon0324)|
<br/>

## Development Environment

### Back-End
- Java 17
- Spring Boot 3.4
- IntelliJ

### Front-End
- React 19
- Vite 6
- Tailwindcss 4.1
- Node.js 22.13
- npm 10.9
- Visual Studio Code

### Communication
- Notion
- Slack
- Discode

### Management
- Git
- GitHub
<br/>

## Tech Stack
### Back-End
<p>
  <img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white" alt="Java" />
  <img src="https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white" alt="Spring Boot" />
  <br/>
</p> 

### Front-End
<p>
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=black" alt="JavaScript" />
  <img src="https://img.shields.io/badge/React-61DAFB?style=for-the-badge&logo=React&logoColor=black" alt="React" />
  <img src="https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=Vite&logoColor=white" alt="Vite" />
  <img src="https://img.shields.io/badge/TailwindCSS-06B6D4?style=for-the-badge&logo=TailwindCSS&logoColor=white" alt="TailwindCSS" />
  <br/>
</p>

### Database
<p>
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white" alt="MySQL" />
  <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white" alt="Redis" />
  <br/>
</p>

### Infrastructure
<p>
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/AWS-232F3E?style=for-the-badge&logo=AmazonAWS&logoColor=white" alt="AWS" />
</p>
<br/>

## API
| API                     | 설명                                                                                  |
|--------------------------|-------------------------------------------------------------------------------------|
| Social Login (Google, Naver, Kakao) | 소셜 계정을 통한 간편 로그인 기능 제공                                                        |
| Naver News Search        | 뉴스 사설을 발행하는 크리에이터의 창작 활동을 지원하기 위해 참고용 뉴스를 검색·제공             |
| OpenAI Chat GPT          | 인기 뉴스 TOP 10을 매일 오전 6시에 자동 요약하여 요약 뉴스 서비스 제공                                           |
| Elasticsearch            | 빠른 검색 및 인기 검색어 기능 제공                                                          |
| AWS SES                 | 회원가입, 비밀번호 변경 등 유저 인증을 위한 이메일 인증 제공                                               |
| AWS S3                  | 프로젝트 내 이미지 파일 스토리지                                                            |
| PortOne                 | 추후 구독 시스템 도입 시 결제 시스템 제공                                                       |

<br/>

## ERD Diagram
<img width="1072" alt="erd-diagram" src="https://github.com/user-attachments/assets/77df6220-c25c-4f4c-b780-59f54237bc6c" />
<br/>
<br/>

## 3 Tier - Architecture
<img width="1501" alt="3TierArchitectuere" src="https://github.com/user-attachments/assets/79e5fb83-1c8e-44dc-969e-ae1e119d993d" />
<br/>
<br/>

## API Docs
👉 [API Docs 바로가기](https://delightful-starburst-80a825.netlify.app/)
<br/>
<br/>

## Responsibilities
이종현
- 프로젝트 아키텍처 설계
- 데이터베이스 모델링
- RESTful API 서버 설계 및 구현
- Client 서버 설계 및 구현
- 인증 및 인가(Spring Serurity, OAuth2, JWT) 기능 구현
- AI 뉴스 핵심 요약 서비스 구현
- 알림 및 상호작용 서비스 구현
<br>

구승회
- 메인 뉴스 서비스 구현
- Elasticsearch 기반 검색 기능 구현
- Swagger를 활용한 API 문서화
- 프론트엔드 CSS 스타일링
<br>

송명보
- 커뮤니티 게시판 기능 구현
- 사용자 투표 기능 구현
- 결제 시스템 연동 및 구현
- 프론트엔드 CSS 스타일링
<br/>

정지원
- 관리자 기능 개발 및 관리 시스템 구축
- 프론트엔드 CSS 스타일링
<br/>

## Preview
👉 [NEWSION Client Repository 바로가기](https://github.com/Heeyeon205/newsion-client)
<br/>
<br/>

## Troubleshooting

### 1. Circular Reference
  <img width="721" alt="스크린샷 2025-05-05 15 20 59" src="https://github.com/user-attachments/assets/262ea73c-3793-4ec6-9930-9d349a7e32b8" /> <br/>
- 댓글 생성 기능 개발 중 BoardService → CommentService → 다시 BoardService가 호출되면서,  
  Spring 빈 생성 시  순환 참조 발생

  <img width="679" alt="스크린샷 2025-05-05 15 33 27" src="https://github.com/user-attachments/assets/2f3bf3ff-ee21-4e1d-8f14-2588941b7433" /> <br/>
```java
@Service
@RequiredArgsConstructor
public class BoardCommentManager {
    private final BoardService boardService;
    private final CommentService commentService;
    private final UserService userService;
    private final NoticeService noticeService;

    @Transactional
    public CommentResponse createComment(Long boardId, Long userId, String commentContent) {
        Board board = boardService.findById(boardId);
        User user = userService.findByUserId(userId);

        Comment comment = Comment.builder()
                .board(board)
                .user(user)
                .content(commentContent)
                .build();
        Comment savedComment = commentService.save(comment);

        User sender = userService.findByUserId(userId);
        User receiver = userService.findByUserId(board.getUser().getId());
        String url = "/board/" + board.getId();
        noticeService.createAndSendNotice(sender, receiver, Notice.Type.BOARD_COMMENT, url, "회원님의 게시글에 댓글을 달았습니다.");
        return new CommentResponse(savedComment);
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentService.findById(commentId);
        userService.matchUserId(comment.getUser().getId(), userId);
        commentService.delete(comment);
    }
}
```
- 댓글과 연관된 공통 역할을 수행하는 `BoardCommentManager` 클래스를 새로 생성.  
  BoardService, CommentService, UserService, NoticeService를 모두 Manager에 주입하고,  
  각 서비스들은 본연의 역할만 수행하며, 공통 로직은 매니저 클래스에서 처리한다.


비즈니스 로직의 일관성과 캡슐화를 유지하기 위해 각 서비스끼리만 의존하는 개발 과정에서 발생한 문제였으며, 이를 해결하기 위해 위와 같은 구조로 리팩토링하여 순환 참조를 피했다. 하지만 최초의 프로젝트 설계부분에서 좀 더 명확히 책임을 분리하는 방법으로 사전에 예방하는 것이 더 나은 방법이라고 생각한다.
<br/>
<br/>

## Future Improvements
정기 구독 서비스 도입
- 결제 모델과 연계된 AI 요약 뉴스 서머리를 구독 고객에게 매일 아침 뉴스레터로 발송

크리에이터 신청 기능
- 일반 사용자가 작성한 사설 및 아티클을 관리자에게 제출하여, 크리에이터 권한을 신청할 수 있는 기능 제공

온라인 토론 이벤트
- 한 주간 가장 많은 투표를 기록한 주제로 온라인 토론 이벤트 진행
<br/>
