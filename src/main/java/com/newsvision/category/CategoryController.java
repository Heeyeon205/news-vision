package com.newsvision.category;

import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@Tag(name = "카테고리 컨트롤러", description = "카테고리 관리 API")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "카테고리 전체 조회",
            description = "등록된 모든 카테고리를 조회합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findAll()));
    }

    @Operation(
            summary = "카테고리 추가",
            description = "새로운 카테고리를 추가합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse saved = categoryService.create(request);
        return ResponseEntity.ok(ApiResponse.success(saved));
    }

    @Operation(
            summary = "카테고리 삭제",
            description = "카테고리를 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("카테고리 삭제 완료 (id: " + id + ")"));
    }


    @Operation(
            summary = "카테고리 수정",
            description = "기존 카테고리를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {
        CategoryResponse updated = categoryService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }
}
