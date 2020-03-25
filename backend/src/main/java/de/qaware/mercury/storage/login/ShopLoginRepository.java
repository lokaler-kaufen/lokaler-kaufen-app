package de.qaware.mercury.storage.login;

import de.qaware.mercury.business.login.ShopLogin;
import org.springframework.lang.Nullable;

public interface ShopLoginRepository {
    void insert(ShopLogin shopLogin);

    @Nullable
    ShopLogin findByEmail(String email);

    ShopLogin findById(ShopLogin.Id id);

    void update(ShopLogin shopLogin);
}
