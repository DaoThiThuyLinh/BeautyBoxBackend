package org.beautybox.repository;

import org.beautybox.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query(value = "SELECT COUNT(*) > 0 FROM Cart c " +
            "WHERE c.user.id = :userId " +
            "and c.productDetail.id = :productDetailId")
    boolean existsByUserAndProductDetail(String userId, String productDetailId);

    @Query("FROM Cart c " +
            "WHERE c.user.id = :userId ")
    List<Cart> findByUserId(String userId);
}
