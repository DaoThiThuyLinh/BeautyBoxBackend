package org.beautybox.mapper;

import org.beautybox.entity.Brand;
import org.beautybox.repository.ProductRepository;
import org.beautybox.response.BrandResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class BrandMapper {
    @Autowired
    ProductRepository productRepository;

    @Mapping(target = "totalProduct", expression = "java(productRepository.countByBrand(brand.getId()))")
    public abstract BrandResponse toResponse(Brand brand);
}
