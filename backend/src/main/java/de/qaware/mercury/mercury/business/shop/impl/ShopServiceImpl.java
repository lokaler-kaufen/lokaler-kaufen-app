package de.qaware.mercury.mercury.business.shop.impl;

import de.qaware.mercury.mercury.business.email.EmailService;
import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.GeoLocationLookup;
import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopListEntry;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.mercury.storage.shop.ShopRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
class ShopServiceImpl implements ShopService {
    private final UUIDFactory uuidFactory;
    private final GeoLocationLookup geoLocationLookup;
    private final ShopRepository shopRepository;
    private final EmailService emailService;

    ShopServiceImpl(UUIDFactory uuidFactory, GeoLocationLookup geoLocationLookup, ShopRepository shopRepository, EmailService emailService) {
        this.uuidFactory = uuidFactory;
        this.geoLocationLookup = geoLocationLookup;
        this.shopRepository = shopRepository;
        this.emailService = emailService;
    }

    @Override
    public List<Shop> listAll() {
        return shopRepository.listAll();
    }

    @Override
    public List<ShopListEntry> findNearby(String postCode) {
        GeoLocation location = geoLocationLookup.fromZipCode(postCode);
        return shopRepository.findNearby(location.getLatitude(), location.getLongitude());
    }

    @Override
    public void delete(Shop.Id id) throws ShopNotFoundException {
        Shop shop = shopRepository.findById(id);
        if (shop == null) {
            throw new ShopNotFoundException(id);
        }

        shopRepository.deleteById(id);
    }

    @Override
    @Nullable
    public Shop findById(Shop.Id id) {
        return shopRepository.findById(id);
    }

    @Override
    public Shop create(String name, String ownerName, String email, String street, String zipCode, String city, String addressSupplement, List<ContactType> contactTypes) {
        UUID id = uuidFactory.create();

        GeoLocation geoLocation = geoLocationLookup.fromZipCode(zipCode);
        Shop shop = new Shop(Shop.Id.of(id), name, ownerName, email, street, zipCode, city, addressSupplement, contactTypes, false, geoLocation);

        shopRepository.insert(shop);
        return shop;
    }

    @Override
    public Shop update(Shop.Id id, String name, String ownerName, String email, String street, String zipCode, String city, String addressSupplement, List<ContactType> contactTypes, boolean enabled) {
        GeoLocation geoLocation = geoLocationLookup.fromZipCode(zipCode);
        Shop shop = new Shop(id, name, ownerName, email, street, zipCode, city, addressSupplement, contactTypes, enabled, geoLocation);

        shopRepository.update(shop);
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

    @Override
    public void sendCreateLink(String email) {
        log.info("Sending shop creation link to '{}'", email);

        emailService.sendShopCreationLink(email);
    }
}
