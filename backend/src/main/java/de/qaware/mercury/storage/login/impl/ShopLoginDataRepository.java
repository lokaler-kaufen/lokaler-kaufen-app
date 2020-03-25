package de.qaware.mercury.storage.login.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.UUID;

public interface ShopLoginDataRepository extends JpaRepository<ShopLoginEntity, UUID> {
    @Nullable
    ShopLoginEntity findFirstByEmail(String email);
}
