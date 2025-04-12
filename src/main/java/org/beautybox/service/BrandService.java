package org.beautybox.service;

import org.beautybox.request.CreateBrandRequest;
import org.beautybox.response.BrandResponse;

import java.util.List;

public interface BrandService {
    void addBrand(CreateBrandRequest request);
    String deleteBrand(String brandId);
    List<BrandResponse> getAllBrands();
}
