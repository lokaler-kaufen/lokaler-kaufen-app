package de.qaware.mercury.business.login.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import de.qaware.mercury.business.login.PasswordHasher;
import org.springframework.stereotype.Service;

@Service
class BCryptPasswordHasher implements PasswordHasher {
    private static final int COST = 12;

    @Override
    public String hash(String password) {
        return BCrypt.withDefaults().hashToString(COST, password.toCharArray());
    }

    @Override
    public boolean verify(String password, String hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hash);
        return result.verified;
    }
}
