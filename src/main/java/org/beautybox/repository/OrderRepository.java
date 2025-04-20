package org.beautybox.repository;

import org.beautybox.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderProduct, String> {
    @Query(value = "SELECT COALESCE(SUM(op.quantity), 0) " +
            "FROM OrderProduct op " +
            "WHERE op.productId = :productId " +
            "AND op.status not in (5, 6) ")
    long countByProductId(String productId);

    boolean existsByOrderCode(String orderCode);

    @Query(value = "SELECT COALESCE(SUM(op.quantity), 0) " +
            "FROM OrderProduct op " +
            "WHERE op.productDetailId = :productDetailId " +
            "AND op.status not in (5, 6) ")
    long countByProductDetailId(String productDetailId);

    @Query(value = "FROM OrderProduct op " +
            "WHERE op.user.id = :userId " +
            "ORDER BY op.createdAt desc ")
    List<OrderProduct> findByUserId(String userId);
}
