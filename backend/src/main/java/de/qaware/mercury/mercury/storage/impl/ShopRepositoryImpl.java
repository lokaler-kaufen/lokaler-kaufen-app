package de.qaware.mercury.mercury.storage.impl;

import de.qaware.mercury.mercury.business.Shop;
import de.qaware.mercury.mercury.storage.ShopRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class ShopRepositoryImpl implements ShopRepository {
    @Override
    public List<Shop> listAll() {
        return List.of(
            new Shop(Shop.Id.of(UUID.randomUUID()), "Moes Whiskyladen"),
            new Shop(Shop.Id.of(UUID.randomUUID()), "Flos Kaffeeladen")
        );
    }
}
