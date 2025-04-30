package com.newsvision.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardLikeResponse {
    private int LikeCount;
    private Boolean isLike;
}
