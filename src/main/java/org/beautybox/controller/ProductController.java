package org.beautybox.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateProductDetailRequest;
import org.beautybox.request.CreateProductRequest;
import org.beautybox.request.UpdateProductDetailRequest;
import org.beautybox.request.UpdateProductRequest;
import org.beautybox.response.ApiResponse;
import org.beautybox.service.ProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    final ProductService productService;

    @Operation(summary = "Thêm sản phẩm mới", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping("/admin-api/product")
    public ApiResponse createProduct(@ModelAttribute @Valid CreateProductRequest request) throws BeautyBoxException {
        productService.add(request);
        return ApiResponse.success("Success");
    }

    @Operation(summary = "Thêm ảnh cho sản phẩm", description = "Param form data")
    @PostMapping("/admin-api/product/image/{productId}")
    public ApiResponse createNewImage(@PathVariable String productId,
                                      @RequestParam List<MultipartFile> images) throws BeautyBoxException {
        int cntSuccess = productService.addNewImage(productId, images);
        return ApiResponse.success("Thêm " + cntSuccess + " ảnh thành công");
    }

    @Operation(summary = "Cập nhập thông tin sản phẩm cha")
    @PutMapping("/admin-api/product")
    public ApiResponse updateProduct(@RequestBody @Valid UpdateProductRequest updateProductRequest) throws BeautyBoxException {
        productService.updateProduct(updateProductRequest);
        return ApiResponse.success("Cập nhập thành công") ;
    }

    @Operation(summary = "Thêm sản phẩm con", security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PostMapping("/admin-api/product-detail")
    public ApiResponse createProductDetail(@ModelAttribute @Valid CreateProductDetailRequest request) throws BeautyBoxException {
        return ApiResponse.success("Created product detail success", productService.addProductDetail(request));
    }

    @Operation(summary = "Cập nhập thông tin sản phẩm con")
    @PutMapping("/admin-api/product-detail")
    public ApiResponse updateProductDetail(@RequestBody @Valid UpdateProductDetailRequest updateRequest) throws BeautyBoxException {
        productService.updateProductDetail(updateRequest);
        return ApiResponse.success("Cập nhập thành công");
    }

    @DeleteMapping("/admin-api/product-detail/{productDetailId}")
    public ApiResponse deleteProductDetail(@PathVariable String productDetailId) throws BeautyBoxException {
        productService.deleteProductDetail(productDetailId);
        return ApiResponse.success("Xoá chi tiết sản phẩm thành công");
    }

    @DeleteMapping("/admin-api/product/{productId}")
    public ApiResponse deleteProduct(@PathVariable String productId) throws BeautyBoxException {
        productService.deleteProduct(productId);
        return ApiResponse.success("Xoá thông tin sản phẩm thành công");
    }

    @Operation(summary = "Xoá ảnh của sản phẩm cha")
    @DeleteMapping("/admin-api/product/image/{imageId}")
    public ApiResponse deleteProductImage(@PathVariable String imageId) throws BeautyBoxException {
        productService.deleteImage(imageId);
        return ApiResponse.success("Xoá thành công");
    }

    @Operation(summary = "Lọc sản phẩm", parameters = {
            @Parameter(name = "orderBy", description = "<h4>Truyền vào giá trị từ 1->5</h4>" +
                    "{1}. Sắp xếp theo thời gian tạo </br>" +
                    "{2}. Sắp xếp theo giá sản phẩm </br>" +
                    "{3}. Sắp xếp theo tên </br>" +
                    "{4}. Sắp xếp theo số lượt mua </br>" +
                    "{5}. Sắp xếp theo lượt đánh giá"),
            @Parameter(name = "value", description = "Từ khoá muốn tìm kiếm, null nếu muốn lấy ra tất cả"),
            @Parameter(name = "sortDirection", description = "acs/desc"),
            @Parameter(name = "category", description = "Tìm theo thể loại, null để lấy ra tất cả "),
            @Parameter(name = "brand", description = "Tìm theo thể loại, null để lấy ra tất cả ")

    })
    @GetMapping("/public-api/product/filter")
    public ApiResponse filterProduct(@RequestParam(required = false) String value,
                                     @RequestParam(required = false) String category,
                                     @RequestParam(required = false) String brand,
                                     @RequestParam(required = false, defaultValue = "0 ") long minPrice,
                                     @RequestParam(required = false, defaultValue = "999999999") long maxPrice,
                                     @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                     @RequestParam(required = false, defaultValue = "40") int pageSize,
                                     @RequestParam(required = false, defaultValue = "1") String orderBy,
                                     @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        String properties = this.getOrderBy(orderBy);
        this.validPage(pageIndex, pageSize);
        return ApiResponse.success("Filter product success", productService.filterProduct(value, category, brand, minPrice, maxPrice, pageIndex, pageSize, properties, sortDirection));
    }

    @GetMapping("/public-api/product/suggest")
    @Operation(summary = "Gợi ý tìm kiếm theo tên")
    public List<String> suggestNameSearch(@RequestParam String value){
        return productService.suggestNameSearch(value);
    }

    @GetMapping("/public-api/product/{productId}")
    @Operation(summary = "Lấy sản phẩm con bằng id sản phẩm cha")
    public ApiResponse getProductDetail(@PathVariable String productId) throws BeautyBoxException {
        return ApiResponse.success("Success", productService.getProductDetail(productId));
    }

    private void validPage(int pageIndex, int pageSize){
        if(pageIndex < 1 || pageSize < 1){
            throw new IllegalArgumentException("Page index or Page size is less than 1");
        }
    }
    private String getOrderBy(String orderBy){
        return switch (orderBy) {
            case "1" -> "createdAt";
            case "2" -> "productDetails.price";
            case "3" -> "name_sort";
            case "4" -> "totalSold";
            case "5" -> "totalReview";
            default -> throw new IllegalArgumentException("Unknown orderBy: " + orderBy);
        };
    }
}
