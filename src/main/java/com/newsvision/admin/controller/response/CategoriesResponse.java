package com.newsvision.admin.controller.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoriesResponse {
    private Long id;
    private String name;
}
