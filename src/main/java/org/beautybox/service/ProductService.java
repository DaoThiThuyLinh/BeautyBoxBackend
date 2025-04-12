package org.beautybox.service;

import org.beautybox.request.CreateProductDetailRequest;
import org.beautybox.request.CreateProductRequest;
import org.beautybox.response.PageResponse;
import org.beautybox.response.ProductDetailResponse;

import java.util.List;

public interface ProductService {
    void add(CreateProductRequest productRequest);
    void addProductDetail(CreateProductDetailRequest productDetailRequest);
    PageResponse<?> filterProduct(String value, String category, String brand, long minPrice, long maxPrice, int pageIndex, int pageSize, String orderBy, String direction);
    List<String> suggestNameSearch(String value);
    List<ProductDetailResponse> getProductDetail(String productId);
}
