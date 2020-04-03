package de.qaware.mercury.storage.admin.impl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;

import java.util.UUID;

public interface AdminDataRepository extends JpaRepository<AdminEntity, UUID> {
    @Nullable
    @Query("SELECT a FROM AdminEntity a WHERE lower(a.email) = lower(:email)")
    AdminEntity findFirstByEmail(@Param("email") String email);
}
