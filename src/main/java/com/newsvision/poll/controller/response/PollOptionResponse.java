package com.newsvision.poll.controller.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollOptionResponse {
    private Long id;
    private String content;
    private int count;
}