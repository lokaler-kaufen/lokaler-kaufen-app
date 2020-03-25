package de.qaware.mercury.business.login;

import de.qaware.mercury.business.admin.Admin;
import org.springframework.lang.Nullable;

public interface AdminLoginService {
    Admin createLogin(String email, String password);

    AdminToken login(String email, String password) throws LoginException;

    Admin verify(AdminToken token) throws LoginException;

    @Nullable
    Admin findByEmail(String email);
}
