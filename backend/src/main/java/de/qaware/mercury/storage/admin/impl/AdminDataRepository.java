package de.qaware.mercury.storage.admin.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;

import java.util.UUID;

public interface AdminDataRepository extends JpaRepository<AdminEntity, UUID> {
    @Nullable
    AdminEntity findFirstByEmail(String email);
}
