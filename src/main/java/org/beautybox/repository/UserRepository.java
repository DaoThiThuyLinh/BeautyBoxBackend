package org.beautybox.repository;

import org.beautybox.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findUserByEmail(String email);
    Boolean existsByEmail(String email);
    @Query("SELECT u, COUNT(o) totalOrder, COALESCE(SUM(oi.price - (oi.price * oi.discount/ 100)),0) totalRevenue " +
            "FROM User u " +
            "LEFT JOIN u.orders o " +
            "LEFT JOIN o.orderItems oi " +
            "WHERE ('' = :name OR u.name = :name) " +
            "GROUP BY u " +
            "ORDER BY :sort " )
    Page<Object[]> findUsersByName(String name, Pageable pageable, String sort);
}
