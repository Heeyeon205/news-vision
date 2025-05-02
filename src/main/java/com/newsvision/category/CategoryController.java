package com.newsvision.category;

import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
@Tag(name = "CategoryController", description = "카테고리 관리 API")
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(summary = "카테고리 불러오기", description = "카테고리 불러오기")
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoryService.findAll()));
    }

    @Operation(summary = "카테고리 추가", description = "카테고리 추가")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@RequestBody CategoryRequest request) {
        CategoryResponse saved = categoryService.create(request);
        return ResponseEntity.ok(ApiResponse.success(saved));
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("카테고리 삭제 완료 (id: " + id + ")"));
    }

    @Operation(summary = "카테고리 수정", description = "카테고리 수정")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(
            @PathVariable Long id,
            @RequestBody CategoryRequest request
    ) {
        CategoryResponse updated = categoryService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

}
