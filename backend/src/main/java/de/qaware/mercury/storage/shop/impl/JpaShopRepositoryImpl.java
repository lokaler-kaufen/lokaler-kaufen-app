package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.location.BoundingBox;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.storage.shop.ShopRepository;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Null;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaShopRepositoryImpl implements ShopRepository {
    private final ShopDataRepository shopDataRepository;
    private final Clock clock;

    @Override
    public List<Shop> listAll() {
        return Lists.map(shopDataRepository.findAll(), ShopEntity::toShop);
    }

    @Override
    public void insert(Shop shop) {
        log.debug("Insert {}", shop);
        shopDataRepository.save(ShopEntity.of(shop.withCreated(clock.nowZoned())));
    }

    @Override
    @Nullable
    public Shop findById(de.qaware.mercury.business.shop.Shop.Id id) {
        log.debug("Find Shop {}", id);
        ShopEntity entity = shopDataRepository.findById(id.getId()).orElse(null);
        return Null.map(entity, ShopEntity::toShop);
    }

    @Override
    public List<Shop> findApproved() {
        log.debug("Finding approved shops");

        List<ShopEntity> shops = shopDataRepository.findApproved();
        return shops.stream().map(ShopEntity::toShop).collect(Collectors.toList());
    }

    @Override
    public List<Shop> findApproved(BoundingBox searchArea) {
        log.debug("Finding shops nearby location {}", searchArea);

        List<ShopEntity> shops = shopDataRepository.findApproved(searchArea.getNorthEast().getLatitude(), searchArea.getNorthEast().getLongitude(),
            searchArea.getSouthWest().getLatitude(), searchArea.getSouthWest().getLongitude());
        return Lists.map(shops, ShopEntity::toShop);
    }

    @Override
    public List<Shop> search(String query) {
        List<ShopEntity> shops = shopDataRepository.search("%" + query + "%");
        return Lists.map(shops, ShopEntity::toShop);
    }

    @Override
    public List<Shop> search(String query, BoundingBox searchArea) {
        List<ShopEntity> shops = shopDataRepository.search("%" + query + "%",
            searchArea.getNorthEast().getLatitude(), searchArea.getNorthEast().getLongitude(),
            searchArea.getSouthWest().getLatitude(), searchArea.getSouthWest().getLongitude());
        return Lists.map(shops, ShopEntity::toShop);
    }

    @Override
    public void update(Shop updatedShop) {
        log.debug("Update {}", updatedShop);
        shopDataRepository.save(ShopEntity.of(updatedShop.withUpdated(clock.nowZoned())));
    }

    @Override
    public void deleteById(Shop.Id id) {
        shopDataRepository.deleteById(id.getId());
    }

    @Override
    public List<Shop> findByName(String name) {
        return Lists.map(shopDataRepository.findByName(name), ShopEntity::toShop);
    }

}
