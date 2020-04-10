package de.qaware.mercury.business.login;

import de.qaware.mercury.business.admin.Admin;
import org.springframework.lang.Nullable;

public interface AdminLoginService {
    Admin createLogin(String email, String password, AdminEmailSettings emailSettings);

    VerifiedToken<Admin.Id, AdminToken> login(String email, String password) throws LoginException;

    Admin verify(AdminToken token) throws LoginException;

    @Nullable
    Admin findByEmail(String email);

    @Nullable
    VerifiedToken<Admin.Id, AdminToken> getTokenInfo(AdminToken token);
}
