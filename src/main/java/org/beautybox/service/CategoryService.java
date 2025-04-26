package org.beautybox.service;

import org.beautybox.exception.BeautyBoxException;
import org.beautybox.request.CreateCategoryRequest;
import org.beautybox.request.UpdateCategoryRequest;
import org.beautybox.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    void addCategory(CreateCategoryRequest request);
    void updateCategory(UpdateCategoryRequest request) throws BeautyBoxException;
    String deleteCategory(String categoryId);
    List<CategoryResponse> getAllCategories();
}
