package de.qaware.mercury.mercury.business.login;

import de.qaware.mercury.mercury.business.admin.Admin;

public interface AdminLoginService {
    Admin createLogin(String email, String password);

    AdminToken login(String email, String password) throws LoginException;

    Admin verify(AdminToken token) throws LoginException;
}
