package de.qaware.mercury.mercury.storage.login.impl;

import de.qaware.mercury.mercury.business.login.ShopLogin;
import de.qaware.mercury.mercury.business.time.Clock;
import de.qaware.mercury.mercury.storage.login.ShopLoginRepository;
import de.qaware.mercury.mercury.util.Null;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaShopLoginRepository implements ShopLoginRepository {
    private final ShopLoginDataRepository shopLoginDataRepository;
    private final Clock clock;

    @Override
    public void insert(ShopLogin shopLogin) {
        log.debug("Insert shop login {}", shopLogin);
        shopLoginDataRepository.save(ShopLoginEntity.of(shopLogin.withCreated(clock.nowZoned())));
    }

    @Override
    public ShopLogin findByEmail(String email) {
        ShopLoginEntity entity = shopLoginDataRepository.findFirstByEmail(email);

        return Null.map(entity, ShopLoginEntity::toShopLogin);
    }

    @Override
    public ShopLogin findById(ShopLogin.Id id) {
        ShopLoginEntity entity = shopLoginDataRepository.findById(id.getId()).orElse(null);

        return Null.map(entity, ShopLoginEntity::toShopLogin);
    }
}
