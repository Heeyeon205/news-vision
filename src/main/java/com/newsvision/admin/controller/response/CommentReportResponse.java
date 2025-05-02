package com.newsvision.admin.controller.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReportResponse {
    private Long id;                 // 신고 ID
    private String commentId;        // 댓글 ID
    private String commentContent;   // 댓글 내용
    private String userId;           // 신고자 ID
    private String commentWriter;    // 댓글작성자
    private String userNickname;     // 신고자 닉네임
    private Long boardId;            // 게시글 ID (추가)
    private String createdAt;        // 댓글 작성일시 (추가)
}
