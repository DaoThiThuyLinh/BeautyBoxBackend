package org.beautybox.repository;

import org.beautybox.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    @Query("SELECT COALESCE(sum(oi.quantity), 0) " +
            "FROM OrderItem oi " +
            "WHERE oi.productId = :productId " +
            "AND oi.order.status not in (5, 6) ")
    long sumByProductId(String productId);


    @Query("SELECT COALESCE(sum(oi.quantity), 0) " +
            "FROM OrderItem oi " +
            "WHERE oi.productDetailId = :productDetailId " +
            "AND oi.order.status not in (5, 6) ")
    long sumByProductDetailId(String productDetailId);


    @Query("SELECT COALESCE(sum( (oi.price - oi.price * oi.discount/ 100) * oi.quantity), 0) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.status not in (5, 6) ")
    long sumRevenue();

    @Query("SELECT COALESCE(sum( (oi.price - oi.price * oi.discount/ 100) * oi.quantity), 0) " +
            "FROM OrderItem oi " +
            "WHERE oi.order.status not in (5, 6) " +
            "AND oi.order.createdAt >= :startTime " +
            "AND oi.order.createdAt <= :endTime ")
    long sumRevenueByTime(LocalDateTime startTime, LocalDateTime endTime);
}
