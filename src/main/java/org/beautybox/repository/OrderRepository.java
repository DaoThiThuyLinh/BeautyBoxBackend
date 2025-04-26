package org.beautybox.repository;

import org.beautybox.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderProduct, String> {

    boolean existsByOrderCode(String orderCode);

    @Query(value = "FROM OrderProduct op " +
            "WHERE op.user.id = :userId " +
            "AND ( 0 = :status or op.status = :status )" +
            "ORDER BY op.createdAt desc ")
    List<OrderProduct> findByUserIdAndStatus(String userId, int status);
}
