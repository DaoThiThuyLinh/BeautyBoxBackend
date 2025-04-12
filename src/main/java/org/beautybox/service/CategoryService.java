package org.beautybox.service;

import org.beautybox.request.CreateCategoryRequest;
import org.beautybox.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    void addCategory(CreateCategoryRequest request);
    String deleteCategory(String categoryId);
    List<CategoryResponse> getAllCategories();
}
