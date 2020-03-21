package de.qaware.mercury.mercury.business.shop.impl;

import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.GeoLocationLookup;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.business.shop.ShopWithDistance;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.mercury.storage.shop.ShopRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
class ShopServiceImpl implements ShopService {
    private final UUIDFactory uuidFactory;
    private final GeoLocationLookup geoLocationLookup;
    private final ShopRepository shopRepository;

    ShopServiceImpl(UUIDFactory uuidFactory, GeoLocationLookup geoLocationLookup, ShopRepository shopRepository) {
        this.uuidFactory = uuidFactory;
        this.geoLocationLookup = geoLocationLookup;
        this.shopRepository = shopRepository;
    }

    @Override
    public List<Shop> listAll() {
        return shopRepository.listAll();
    }

    @Override
    public List<ShopWithDistance> findNearby(String postCode) {
        GeoLocation location = geoLocationLookup.fromZipCode(postCode);
        return shopRepository.findNearby(location.getLatitude(), location.getLongitude());
    }

    @Override
    public Shop create(String name, String street, String zipCode, String city) {
        UUID id = uuidFactory.create();

        GeoLocation geoLocation = geoLocationLookup.fromZipCode(zipCode);
        Shop shop = new Shop(Shop.Id.of(id), name, false, geoLocation);

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
