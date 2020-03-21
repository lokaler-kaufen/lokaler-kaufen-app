package de.qaware.mercury.mercury.storage.shop.impl;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.storage.shop.ShopRepository;
import de.qaware.mercury.mercury.util.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
class JpaShopRepositoryImpl implements ShopRepository {
    private final ShopDataRepository shopDataRepository;

    JpaShopRepositoryImpl(ShopDataRepository shopDataRepository) {
        this.shopDataRepository = shopDataRepository;
    }

    @Override
    public List<Shop> listAll() {
        return Lists.map(shopDataRepository.findAll(), ShopEntity::toShop);
    }

    @Override
    public void insert(Shop shop) {
        log.debug("Insert {}", shop);
        shopDataRepository.save(ShopEntity.of(shop));
    }

    @Override
    public Shop findById(Shop.Id id) {
        log.debug("Find Shop {}", id);
        Optional<ShopEntity> shopEntityOptional = shopDataRepository.findById(id.getId());
        ShopEntity shopEntity = shopEntityOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return shopEntity.toShop();
    }
}
