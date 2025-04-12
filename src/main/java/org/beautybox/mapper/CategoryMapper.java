package org.beautybox.mapper;

import org.beautybox.entity.Category;
import org.beautybox.repository.CategoryRepository;
import org.beautybox.repository.ProductRepository;
import org.beautybox.response.CategoryResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class CategoryMapper {
    @Autowired
    ProductRepository productRepository;

    @Mapping(target = "totalProduct", expression = "java(productRepository.countByCategory(category.getId()))")
    public abstract CategoryResponse toResponse(Category category);
}
