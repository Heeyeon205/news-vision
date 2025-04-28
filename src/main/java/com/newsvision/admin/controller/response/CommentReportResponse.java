package com.newsvision.admin.controller.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentReportResponse {

    private Long id;                // 신고 ID
    private String commentId;        // 댓글 ID
    private String commentContent;   // 댓글 내용 추가
    private String userId;           // 신고자 ID
    private String userNickname;     // 신고자 닉네임 추가
}