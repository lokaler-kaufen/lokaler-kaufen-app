package de.qaware.mercury.mercury.business.impl;

import de.qaware.mercury.mercury.business.Shop;
import de.qaware.mercury.mercury.business.ShopService;
import de.qaware.mercury.mercury.storage.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ShopServiceImpl implements ShopService {
    private final ShopRepository shopRepository;

    ShopServiceImpl(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public List<Shop> listAll() {
        return shopRepository.listAll();
    }
}
