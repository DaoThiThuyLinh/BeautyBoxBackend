package org.beautybox.service;

import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateBrandRequest;
import org.beautybox.request.UpdateBrandRequest;
import org.beautybox.response.BrandResponse;

import java.io.IOException;
import java.util.List;

public interface BrandService {
    void addBrand(CreateBrandRequest request);
    void updateBrand(UpdateBrandRequest updateRequest) throws BeautyBoxException, IOException;
    String deleteBrand(String brandId);
    List<BrandResponse> getAllBrands();
}
