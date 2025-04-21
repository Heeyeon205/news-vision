package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.CategoriesResponse;
import com.newsvision.admin.service.CategoriesService;
import com.p6spy.engine.logging.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;

    @GetMapping
    public ResponseEntity<List<CategoriesResponse>> getCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }

}
