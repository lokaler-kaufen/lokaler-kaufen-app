package de.qaware.mercury.mercury.business.impl;

import de.qaware.mercury.mercury.business.Shop;
import de.qaware.mercury.mercury.business.ShopService;
import de.qaware.mercury.mercury.business.UUIDFactory;
import de.qaware.mercury.mercury.storage.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class ShopServiceImpl implements ShopService {
    private final UUIDFactory uuidFactory;
    private final ShopRepository shopRepository;

    ShopServiceImpl(UUIDFactory uuidFactory, ShopRepository shopRepository) {
        this.uuidFactory = uuidFactory;
        this.shopRepository = shopRepository;
    }

    @Override
    public List<Shop> listAll() {
        return shopRepository.listAll();
    }

    @Override
    public Shop create(String name) {
        UUID id = uuidFactory.create();
        Shop shop = new Shop(Shop.Id.of(id), name);

        shopRepository.insert(shop);
        return shop;
    }
}
