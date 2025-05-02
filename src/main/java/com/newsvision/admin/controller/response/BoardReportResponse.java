package com.newsvision.admin.controller.response;

import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BoardReportResponse {
    private Long id;              // 신고 ID
    private Long boardId;         // 게시글 ID
    private String boardWriter;   // 게시글 작성자 닉네임
    private String boardCreatedAt; // 게시글 작성일
    private String userNickname;          // 신고자 닉네임
}
