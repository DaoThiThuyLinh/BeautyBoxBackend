package org.beautybox.repository;

import org.beautybox.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, String> {
    boolean existsByName(String name);
}
