package org.beautybox.repository;

import org.beautybox.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    @Query("SELECT count(*) > 0 " +
            "FROM Review r " +
            "WHERE r.orderItem.id = :orderItemId ")
    boolean existsByOrderItemId(String orderItemId);

    @Query("FROM Review r " +
            "WHERE r.orderItem.productId = :productId ")
    List<Review> findByProductId(String productId);
}
