package de.qaware.mercury.storage.login.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.UUID;

public interface ShopLoginDataRepository extends JpaRepository<ShopLoginEntity, UUID> {
    @Nullable
    @Query("SELECT s FROM ShopLoginEntity s WHERE lower(s.email) = lower(:email)")
    ShopLoginEntity findFirstByEmail(@Param("email") String email);
}
