package de.qaware.mercury.mercury.storage.admin;

import de.qaware.mercury.mercury.business.admin.Admin;
import org.springframework.lang.Nullable;

public interface AdminRepository {
    @Nullable
    Admin findByEmail(String email);

    @Nullable
    Admin findById(Admin.Id id);

    void insert(Admin admin);
}
