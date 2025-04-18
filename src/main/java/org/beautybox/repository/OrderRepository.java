package org.beautybox.repository;

import org.beautybox.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderProduct, String> {
    @Query(value = "SELECT count(*) " +
            "FROM OrderProduct op " +
            "WHERE op.productId = :productId ")
    long countByProductId(String productId);

    @Query(value = "SELECT count(*)" +
            "FROM OrderProduct op " +
            "WHERE op.productDetailId = :productDetailId")
    long countByProductDetailId(String productDetailId);

    @Query(value = "FROM OrderProduct op " +
            "WHERE op.user.id = :userId ")
    List<OrderProduct> findByUserId(String userId);
}
