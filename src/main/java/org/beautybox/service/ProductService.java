package org.beautybox.service;

import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateProductDetailRequest;
import org.beautybox.request.CreateProductRequest;
import org.beautybox.request.UpdateProductDetailRequest;
import org.beautybox.request.UpdateProductRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.ProductResponse;

import java.util.List;

public interface ProductService {
    void add(CreateProductRequest productRequest) throws BeautyBoxException;
    void updateProduct(UpdateProductRequest productRequest) throws BeautyBoxException;
    void addProductDetail(CreateProductDetailRequest productDetailRequest) throws BeautyBoxException;
    void updateProductDetail(UpdateProductDetailRequest updateRequest) throws BeautyBoxException;
    void deleteProductDetail(String id) throws BeautyBoxException;
    void deleteProduct(String productId) throws BeautyBoxException;
    PageResponse<?> filterProduct(String value, String category, String brand, long minPrice, long maxPrice, int pageIndex, int pageSize, String orderBy, String direction);
    List<String> suggestNameSearch(String value);
    ProductResponse getProductDetail(String productId) throws BeautyBoxException;
}
