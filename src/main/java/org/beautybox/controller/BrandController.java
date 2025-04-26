package org.beautybox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateBrandRequest;
import org.beautybox.request.UpdateBrandRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.BrandService;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @Operation(summary = "Thêm thương hiệu mới", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping("/admin-api/brand")
    public ApiResponse createCategory(@ModelAttribute @Valid CreateBrandRequest request) {
        brandService.addBrand(request);
        return ApiResponse.success("Create success");
    }

    @Operation(summary = "Cập nhập brand", description = "Khi bỏ trống Image sẽ vẫn lấy lại ảnh cũ", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PutMapping("/admin-api/brand")
    public ApiResponse updateCategory(@ModelAttribute @Valid UpdateBrandRequest updateRequest) throws BeautyBoxException, IOException {
        brandService.updateBrand(updateRequest);
        return ApiResponse.success("Update success");
    }

    @Operation(summary = "Xoá thương hiệu kèm sản phẩm thuộc thuơng hiệu này", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @DeleteMapping("/admin-api/brand/{brandId}")
    public ApiResponse deleteCategory(@PathVariable String brandId) {
        String response = brandService.deleteBrand(brandId);
        if(response.equals("success")) {
            return ApiResponse.success("Delete success");
        }else{
            return ApiResponse.error(response);
        }
    }

    @GetMapping("/public-api/brand")
    public ApiResponse getAllBrands() {
        return ApiResponse.success("Get all brand success", brandService.getAllBrands());
    }
}
