package de.qaware.mercury.storage.shop.impl;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShopBreakDataRepository extends JpaRepository<ShopBreakEntity, UUID> {
    List<ShopBreakEntity> findAllByShopId(UUID shopId);
}
