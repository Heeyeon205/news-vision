
package com.newsvision.board.controller.response;
import lombok.Getter;
import lombok.Setter;


import com.newsvision.board.entity.Board;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private Long categoryId;
   private LocalDateTime createdAt;
   private Long userId;
    private String image;
    private Long view;
   private Long newsId;
   private Boolean isReported;
   private Long likeCount;
   private Long commentCount;

   public BoardResponse(Long id, String title, String content, Long categoryId, LocalDateTime createAt, Long userId, String image, Long view, Long newsId, Boolean isReported, Long likeCount, Long commentCount) {
      this.id = id;
       this.title = title;
       this.content = content;
       this.categoryId = categoryId;
      this.createdAt = createAt;
     this.userId = userId;
     this.image = image;
      this.view = view;
       this.newsId = newsId;
      this.isReported = isReported;
     this.likeCount = likeCount;
      this.commentCount = commentCount;
   }
}
