package com.newsvision.board.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardUpdateRequest {
    private String title;
    private String content;
    private Long categoryId;
    private String image;
}
