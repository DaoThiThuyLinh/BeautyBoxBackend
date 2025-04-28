package org.beautybox.repository;

import org.beautybox.entity.DefaultAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultAddressRepository extends JpaRepository<DefaultAddress, String> {
}
