package de.qaware.mercury.mercury.business.shop.impl;

import de.qaware.mercury.mercury.business.email.EmailService;
import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.location.GeoLocationLookup;
import de.qaware.mercury.mercury.business.login.ShopLoginService;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopCreation;
import de.qaware.mercury.mercury.business.shop.ShopListEntry;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.mercury.storage.shop.ShopRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ShopServiceImpl implements ShopService {
    private final UUIDFactory uuidFactory;
    private final GeoLocationLookup geoLocationLookup;
    private final ShopRepository shopRepository;
    private final EmailService emailService;
    private final ShopLoginService shopLoginService;

    @Override
    @Transactional(readOnly = true)
    public List<Shop> listAll() {
        return shopRepository.listAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopListEntry> findNearby(String postCode) {
        GeoLocation location = geoLocationLookup.fromZipCode(postCode);
        return shopRepository.findNearby(location.getLatitude(), location.getLongitude());
    }

    @Override
    @Transactional
    public void delete(Shop.Id id) throws ShopNotFoundException {
        Shop shop = shopRepository.findById(id);
        if (shop == null) {
            throw new ShopNotFoundException(id);
        }

        shopRepository.deleteById(id);
    }

    @Override
    @Nullable
    @Transactional(readOnly = true)
    public Shop findById(Shop.Id id) {
        return shopRepository.findById(id);
    }

    @Override
    @Transactional
    public Shop create(ShopCreation creation) {
        UUID id = uuidFactory.create();

        GeoLocation geoLocation = geoLocationLookup.fromZipCode(creation.getZipCode());
        Shop shop = new Shop(
            Shop.Id.of(id), creation.getName(), creation.getOwnerName(), creation.getEmail(), creation.getStreet(),
            // TODO MKA: Slots, contact types
            creation.getZipCode(), creation.getCity(), creation.getAddressSupplement(), new ArrayList<>(creation.getContactTypes().keySet()),
            false, geoLocation
        );

        shopRepository.insert(shop);
        shopLoginService.createLogin(shop, creation.getEmail(), creation.getPassword());
        return shop;
    }

    @Override
    @Transactional
    public Shop update(Shop shop, ShopUpdate update) {
        GeoLocation geoLocation = geoLocationLookup.fromZipCode(update.getZipCode());
        Shop newShop = new Shop(
            shop.getId(), update.getName(), update.getOwnerName(), shop.getName(), update.getStreet(), update.getZipCode(),
            // TODO MKA: Slots, contact types
            update.getCity(), update.getAddressSupplement(), new ArrayList<>(update.getContactTypes().keySet()),
            shop.isEnabled(), geoLocation
        );

        shopRepository.update(newShop);
        return shop;
    }

    @Override
    @Transactional
    public void changeEnabled(Shop.Id id, boolean enabled) throws ShopNotFoundException {
        Shop shop = shopRepository.findById(id);
        if (shop == null) {
            throw new ShopNotFoundException(id);
        }

        Shop newShop = shop.withEnabled(enabled);
        shopRepository.update(newShop);
    }

    @Override
    @Transactional(readOnly = true)
    public void sendCreateLink(String email) {
        log.info("Sending shop creation link to '{}'", email);

        emailService.sendShopCreationLink(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shop> findByName(String name) {
        return shopRepository.findByName(name);
    }
}
