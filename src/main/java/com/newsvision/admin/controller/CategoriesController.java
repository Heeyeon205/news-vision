package com.newsvision.admin.controller;

import com.newsvision.admin.dto.response.CategoriesResponse;
import com.newsvision.admin.service.CategoriesService;
import com.newsvision.category.Categories;
import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/categories")
@Tag(name = "카테고리 컨트롤러", description = "카테고리 관리 API")
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoriesService categoriesService;
    private final CategoriesService categoryService;

    @Operation(
            summary = "카테고리 전체 조회",
            description = "등록된 모든 카테고리를 조회합니다."
    )
    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriesResponse>>> getCategories(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(ApiResponse.success(categoriesService.getAllCategories()));
    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<CategoriesResponse>>> getMaxCategories(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(ApiResponse.success(categoriesService.getMaxAllCategories()));
    }

    @Operation(
            summary = "카테고리 추가",
            description = "새로운 카테고리를 추가합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PostMapping
    public ResponseEntity<ApiResponse<Categories>> addCategory(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        String name = request.get("name");
        Categories category = categoryService.addCategory(name);
        return ResponseEntity.ok(ApiResponse.success(category));
    }


    @Operation(
            summary = "카테고리 수정",
            description = "기존 카테고리를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categories>> updateCategory(
            @PathVariable Long id,
            @RequestParam String name,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        Categories updated = categoryService.updateCategory(id, name);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @Operation(
            summary = "카테고리 삭제",
            description = "카테고리를 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCategory(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        categoriesService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success("카테고리(ID: " + id + ") 삭제 완료"));
    }}