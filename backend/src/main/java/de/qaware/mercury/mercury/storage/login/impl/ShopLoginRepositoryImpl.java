package de.qaware.mercury.mercury.storage.login.impl;

import de.qaware.mercury.mercury.business.login.ShopLogin;
import de.qaware.mercury.mercury.storage.login.ShopLoginRepository;
import de.qaware.mercury.mercury.util.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class ShopLoginRepositoryImpl implements ShopLoginRepository {
    private final ShopLoginDataRepository shopLoginDataRepository;

    ShopLoginRepositoryImpl(ShopLoginDataRepository shopLoginDataRepository) {
        this.shopLoginDataRepository = shopLoginDataRepository;
    }

    @Override
    public void insert(ShopLogin shopLogin) {
        log.debug("Insert shop login {}", shopLogin);
        shopLoginDataRepository.save(ShopLoginEntity.of(shopLogin));
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
