package org.beautybox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.request.CreateCategoryRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.CategoryService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    final CategoryService categoryService;

    @PostMapping("/admin-api/category")
    @Operation(summary = "Thêm thể loại mới", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    public ApiResponse createCategory(@RequestBody @Valid CreateCategoryRequest request) {
        categoryService.addCategory(request);
        return ApiResponse.success("Create success");
    }

    @DeleteMapping("/admin-api/category/{categoryId}")
    @Operation(summary = "Xoá thể loại kèm theo sản phẩm", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    public ApiResponse deleteCategory(@PathVariable String categoryId) {
        String response = categoryService.deleteCategory(categoryId);
        if(response.equals("success")) {
            return ApiResponse.success("Delete success");
        }
        return ApiResponse.error(response);
    }

    @GetMapping("/public-api/category")
    public ApiResponse getCategory() {
        return ApiResponse.success("Get all categories", categoryService.getAllCategories());
    }
}
