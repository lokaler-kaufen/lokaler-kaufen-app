package de.qaware.mercury.mercury.business.shop.impl;

import de.qaware.mercury.mercury.business.location.Location;
import de.qaware.mercury.mercury.business.location.LocationLookup;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.mercury.storage.shop.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class ShopServiceImpl implements ShopService {
    private final UUIDFactory uuidFactory;
    private final LocationLookup locationLookup;
    private final ShopRepository shopRepository;

    ShopServiceImpl(UUIDFactory uuidFactory, LocationLookup locationLookup, ShopRepository shopRepository) {
        this.uuidFactory = uuidFactory;
        this.locationLookup = locationLookup;
        this.shopRepository = shopRepository;
    }

    @Override
    public List<Shop> listAll() {
        return shopRepository.listAll();
    }

    @Override
    public List<Shop> findNearby(String postCode) {
        Location location = locationLookup.fromPostcode(postCode);
        return shopRepository.findNearby(location.getLatitude(), location.getLongitude());
    }

    @Override
    public Shop create(String name, String postCode, boolean enabled) {
        return create(name, locationLookup.fromPostcode(postCode), enabled);
    }

    @Override
    public Shop create(String name, Location location, boolean enabled) {
        UUID id = uuidFactory.create();
        Shop shop = new Shop(Shop.Id.of(id), name, enabled, location);

        shopRepository.insert(shop);
        return shop;
    }

    @Override
    public void changeEnabled(Shop.Id id, boolean enabled) throws ShopNotFoundException {
        Shop shop = shopRepository.findById(id);
        if (shop == null) {
            throw new ShopNotFoundException(id);
        }

        Shop newShop = shop.withEnabled(enabled);
        shopRepository.update(newShop);
    }
}
