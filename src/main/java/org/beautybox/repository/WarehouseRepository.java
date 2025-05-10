package org.beautybox.repository;

import org.beautybox.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
    @Query("FROM Warehouse w " +
            "WHERE w.productDetail.id = :productDetailId ")
    Page<Warehouse> getAllByProductDetailId(String productDetailId, Pageable pageable);

    @Query("FROM Warehouse w " +
            "WHERE w.productDetail.product.id = :productId " +
            "OR :productId = ''")
    Page<Warehouse> getAllByProductId(String productId, Pageable pageable);

    @Query("SELECT AVG(oi.price - oi.price * oi.discount / 100) " +
            "FROM OrderItem oi " +
            "WHERE oi.productDetailId = :productDetailId " +
            "OR :productDetailId = '' ")
    double getAvgPriceByProductDetailId(String productDetailId);

}
