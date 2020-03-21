package de.qaware.mercury.mercury.storage.shop.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ShopDataRepository extends JpaRepository<ShopEntity, UUID> {
    @Query("select e from ShopEntity e")
    List<Object> queryStuff();
}
