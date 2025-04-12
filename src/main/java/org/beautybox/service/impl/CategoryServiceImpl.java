package org.beautybox.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.beautybox.entity.Category;
import org.beautybox.exception.BeautyBoxException;
import org.beautybox.exception.ErrorDetail;
import org.beautybox.mapper.CategoryMapper;
import org.beautybox.repository.CategoryRepository;
import org.beautybox.request.CreateCategoryRequest;
import org.beautybox.response.CategoryResponse;
import org.beautybox.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    final CategoryRepository categoryRepository;
    final CategoryMapper categoryMapper;

    @SneakyThrows
    @Override
    public void addCategory(CreateCategoryRequest request) {
        if(categoryRepository.existsByName(request.getName())) {
            throw new BeautyBoxException(ErrorDetail.ERR_CATEGORY_EXISTED);
        }
        Category category = new Category();
        category.setName(request.getName());
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(String categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if(category.isPresent()) {
            categoryRepository.delete(category.get());
            return "success";
        }return "Category does not exist";
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream().map(categoryMapper::toResponse).toList();
    }
}
