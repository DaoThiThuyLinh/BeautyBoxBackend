package org.beautybox.repository;

import org.beautybox.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    @Query(value = "SELECT COUNT(*) > 0 FROM Cart c " +
            "WHERE c.user.id = :userId " +
            "and c.productDetail.id = :productDetailId")
    boolean existsByUserAndProductDetail(String userId, String productDetailId);
}
