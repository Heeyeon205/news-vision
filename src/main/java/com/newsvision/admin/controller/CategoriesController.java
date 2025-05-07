package com.newsvision.admin.controller;

import com.newsvision.admin.dto.response.CategoriesResponse;
import com.newsvision.admin.service.CategoriesService;
import com.newsvision.category.Categories;
import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Hidden
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoriesController {
    private final CategoriesService categoriesService;
    private final CategoriesService categoryService;

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