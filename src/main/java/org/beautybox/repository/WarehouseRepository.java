package org.beautybox.repository;

import org.beautybox.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, String> {
    @Query("FROM Warehouse w " +
            "WHERE w.productDetail.id = :productDetailId ")
    Page<Warehouse> getAllByProductId(String productDetailId, Pageable pageable);
}
