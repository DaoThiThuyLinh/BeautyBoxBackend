package org.beautybox.repository;

import org.beautybox.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {
    @Query("from ProductDetail pd " +
            "where pd.product.id = :productId ")
    List<ProductDetail> findByProductId(String productId);
}
