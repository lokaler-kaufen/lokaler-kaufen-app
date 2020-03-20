package de.qaware.mercury.mercury.storage.shop.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ShopDataRepository extends JpaRepository<ShopEntity, UUID> {
}
