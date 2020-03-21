package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.admin.Admin;

public interface TokenService {
    AdminToken createAdminToken(Admin.Id adminId);

    Admin.Id verifyAdminToken(AdminToken token) throws LoginException;
}
