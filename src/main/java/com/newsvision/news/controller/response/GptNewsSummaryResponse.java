package com.newsvision.news.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GptNewsSummaryResponse {
        private String title;
        private String summary;
}
