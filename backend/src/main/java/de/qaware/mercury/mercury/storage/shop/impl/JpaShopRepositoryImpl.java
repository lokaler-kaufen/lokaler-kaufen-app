package de.qaware.mercury.mercury.storage.shop.impl;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopListEntry;
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
    public List<ShopListEntry> findNearby(double latitude, double longitude) {
        log.debug("Find show nearby location {}, {}", longitude, latitude);

        String query = "SELECT " +
            "s.id AS id, " +
            "s.name AS name, " +
            "s.contact_types AS contact_types, " +
            "sqrt(" +
            "   power(111.3 * cos((?1 + s.latitude) / (2*0.01745)) * (?2-s.longitude) , 2) " +
            " + power(111.3*(?1-s.latitude), 2)" +
            ") AS distance " +
            "FROM shop AS s";

        @SuppressWarnings("unchecked")
        List<ShopWithDistance> shops = (List<ShopWithDistance>) entityManager.createNativeQuery(query, "ShopWithDistance") // ShopWithDistance is declared on ShopEntity
            .setParameter(1, latitude)
            .setParameter(2, longitude)
            .getResultList();

        return Lists.map(shops, ShopWithDistance::toShopListEntry);
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
}
