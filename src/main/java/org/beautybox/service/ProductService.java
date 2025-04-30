package org.beautybox.service;

import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateProductDetailRequest;
import org.beautybox.request.CreateProductRequest;
import org.beautybox.request.UpdateProductDetailRequest;
import org.beautybox.request.UpdateProductRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    void add(CreateProductRequest productRequest) throws BeautyBoxException;
    int addNewImage(String productId, List<MultipartFile> images) throws BeautyBoxException;
    void updateProduct(UpdateProductRequest productRequest) throws BeautyBoxException;
    String addProductDetail(CreateProductDetailRequest productDetailRequest) throws BeautyBoxException;
    void updateProductDetail(UpdateProductDetailRequest updateRequest) throws BeautyBoxException;
    void deleteProductDetail(String id) throws BeautyBoxException;
    void deleteProduct(String productId) throws BeautyBoxException;
    void deleteImage(String imageId) throws BeautyBoxException;
    PageResponse<?> filterProduct(String value, String category, String brand, long minPrice, long maxPrice, int pageIndex, int pageSize, String orderBy, String direction);
    List<String> suggestNameSearch(String value);
    ProductResponse getProductDetail(String productId) throws BeautyBoxException;
}
