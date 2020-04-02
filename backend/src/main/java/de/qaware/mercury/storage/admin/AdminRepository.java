package de.qaware.mercury.storage.admin;

import de.qaware.mercury.business.admin.Admin;
import org.springframework.lang.Nullable;

import java.util.List;

public interface AdminRepository {
    @Nullable
    Admin findByEmail(String email);

    @Nullable
    Admin findById(Admin.Id id);

    void insert(Admin admin);

    List<Admin> findWithEmailOnShopApprovalNeeded(boolean value);
}
