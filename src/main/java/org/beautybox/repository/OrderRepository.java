package org.beautybox.repository;

import org.beautybox.entity.OrderProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderProduct, String> {

    boolean existsByOrderCode(String orderCode);

    @Query(value = "SELECT DISTINCT op " +
            "FROM OrderProduct op " +
            "LEFT JOIN op.orderItems odi " +
            "WHERE ( :userId = '' or op.user.id = :userId ) " +
            "AND ( 0 = :status or op.status = :status ) " +
            "AND ( :s = '' or op.id = :s or  odi.productId = :s ) ")
    Page<OrderProduct> getOrders(String s, String userId, int status, Pageable pageable);
}
