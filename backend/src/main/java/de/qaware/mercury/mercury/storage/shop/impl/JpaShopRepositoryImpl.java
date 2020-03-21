package de.qaware.mercury.mercury.storage.shop.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopWithDistance;
import de.qaware.mercury.mercury.storage.shop.ShopRepository;
import de.qaware.mercury.mercury.util.Lists;
import de.qaware.mercury.mercury.util.Null;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class JpaShopRepositoryImpl implements ShopRepository {
    private final ShopDataRepository shopDataRepository;
    private final EntityManager entityManager;

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
    public List<ShopWithDistance> findNearby(GeoLocation location) {
        log.debug("Finding shops nearby location {}", location);

        List<ShopWithDistanceProjection> shops = shopDataRepository.findNearby(location.getLatitude(), location.getLongitude());
        return toShopWithDistance(shops);
    }

    @Override
    public List<ShopWithDistance> search(String query, GeoLocation location) {
        List<ShopWithDistanceProjection> shops = shopDataRepository.search("%" + query + "%", location.getLatitude(), location.getLongitude());
        return toShopWithDistance(shops);

    }

    @Override
    public void update(Shop updatedShop) {
        log.debug("Update {}", updatedShop);
        shopDataRepository.save(ShopEntity.of(updatedShop));
    }

    @Override
    public void deleteById(Shop.Id id) {
        shopDataRepository.deleteById(id.getId());
    }

    @Override
    public List<Shop> findByName(String name) {
        return Lists.map(shopDataRepository.findByName(name), ShopEntity::toShop);
    }

    private List<ShopWithDistance> toShopWithDistance(List<ShopWithDistanceProjection> shops) {
        return Lists.map(shops, s -> new ShopWithDistance(s.getShopEntity().toShop(), s.getDistance()));
    }
}
