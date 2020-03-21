package de.qaware.mercury.mercury.storage.shop.impl;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.storage.shop.ShopRepository;
import de.qaware.mercury.mercury.util.Lists;
import de.qaware.mercury.mercury.util.Null;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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
    @Nullable
    public Shop findById(Shop.Id id) {
        log.debug("Find Shop {}", id);
        ShopEntity entity = shopDataRepository.findById(id.getId()).orElse(null);
        return Null.map(entity, ShopEntity::toShop);
    }

    @Override
    public List<Shop> findNearby(double longitude, double latitude) {
        log.debug("Find show nearby location {}, {}", longitude, latitude);

        Collection<ShopEntity> shops = Collections.emptyList();

        // TODO:  implement this query correctly and transform miles to kilometers
        // first step should use limit 10 and no max distance

/*        SELECT latitude, longitude, SQRT(
            POW(69.1 * (latitude - [startlat]), 2) +
            POW(69.1 * ([startlng] - longitude) * COS(latitude / 57.3), 2)) AS distance
        FROM TableName HAVING distance < maxDistance ORDER BY distance LIMIT maxResults;*/


        return Lists.map(shops, ShopEntity::toShop);
    }

    @Override
    public void update(Shop updatedShop) {
        shopDataRepository.save(ShopEntity.of(updatedShop));
    }
}
