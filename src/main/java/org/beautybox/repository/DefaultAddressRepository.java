package org.beautybox.repository;

import org.beautybox.entity.DefaultAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefaultAddressRepository extends JpaRepository<DefaultAddress, String> {
    @Query("FROM DefaultAddress da " +
            "where da.user.id = :userId ")
    List<DefaultAddress> getByUser(String userId);
}
