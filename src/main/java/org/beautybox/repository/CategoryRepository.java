package org.beautybox.repository;

import org.beautybox.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
    boolean existsByName(String name);
}
