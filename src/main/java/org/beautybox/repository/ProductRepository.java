package org.beautybox.repository;

import org.beautybox.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, String> {

    @Query("from Product p " +
            "inner join ProductDetail pd on p.id = pd.product.id " +
            "where p.name like concat(:name, '%') " +
            "and pd.price >= :minPrice " +
            "and pd.price <= :maxPrice ")
    Page<Product> filterProduct(String name, long minPrice, long maxPrice, Pageable pageable);

    @Query("from Product p " +
            "where p.category.id = :category ")
    Page<Product> getByCategory(String category, Pageable pageable);

    @Query("from Product p " +
            "where p.brand.id = :brand ")
    Page<Product> getByBrand(String brand, Pageable pageable);

    @Query("select count(*)" +
            "from Product p " +
            "where p.category.id= :category ")
    int countByCategory(String category);

    @Query("select count(*) " +
            "from Product p " +
            "where p.brand.id = :brand")
    int countByBrand(String brand);
}
