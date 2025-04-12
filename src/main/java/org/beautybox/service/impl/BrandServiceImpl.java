package org.beautybox.service.impl;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.beautybox.entity.Brand;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.BrandMapper;
import org.beautybox.repository.BrandRepository;
import org.beautybox.request.CreateBrandRequest;
import org.beautybox.response.BrandResponse;
import org.beautybox.service.BrandService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    final BrandRepository brandRepository;
    final BrandMapper brandMapper;
    final Cloudinary cloudinary;

    @SneakyThrows
    @Override
    public void addBrand(CreateBrandRequest request) {
        if(brandRepository.existsByName(request.getName())) {
            throw new BeautyBoxException(ErrorDetail.ERR_BRAND_EXISTED);
        }
        var response = cloudinary.uploader().upload(request.getImage().getBytes(), Map.of());
        Brand brand = new Brand();
        brand.setName(request.getName());
        brand.setDescription(request.getDescription());
        brand.setImgUrl(response.get("url") + "");
        brandRepository.save(brand);
    }

    @Override
    public String deleteBrand(String brandId) {
        Optional<Brand> brand = brandRepository.findById(brandId);
        if(brand.isPresent()) {
            brandRepository.delete(brand.get());
            return "success";
        }
        return "Brand does not exist";
    }

    @Override
    public List<BrandResponse> getAllBrands() {
        return brandRepository.findAll().stream().map(brandMapper::toResponse).toList();
    }
}
