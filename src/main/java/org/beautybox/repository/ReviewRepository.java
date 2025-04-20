package org.beautybox.repository;

import org.beautybox.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
    @Query("SELECT count(*) > 0 " +
            "FROM Review r " +
            "WHERE r.oder.id = :oderId ")
    boolean existsByOrderId(String oderId);
}
