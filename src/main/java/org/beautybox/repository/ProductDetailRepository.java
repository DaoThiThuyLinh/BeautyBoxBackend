package org.beautybox.repository;

import org.beautybox.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, String> {
    @Query("from ProductDetail pd " +
            "where pd.product.id = :productId ")
    List<ProductDetail> findByProductId(String productId);
    @Query("FROM ProductDetail pd " +
            "WHERE pd.id = :productDetailId " +
            "AND pd.isEnabled = true ")
    Optional<ProductDetail> findById(@NonNull String productDetailId);
}
