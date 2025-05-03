package com.newsvision.admin.dto.response;

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
