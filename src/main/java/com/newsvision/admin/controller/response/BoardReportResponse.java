package com.newsvision.admin.controller.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardReportResponse {

    private Long id;              // 신고 ID
    private String boardId;        // 신고당한 게시글 ID
    private String boardContent;   // 신고당한 게시글 내용
    private String userId;         // 신고한 사용자 ID
    private String userNickname;   // 신고한 사용자 닉네임
}