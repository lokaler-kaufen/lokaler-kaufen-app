package de.qaware.mercury.business.shop.impl;

import de.qaware.mercury.business.email.EmailService;
import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.location.LocationService;
import de.qaware.mercury.business.login.ShopLoginService;
import de.qaware.mercury.business.shop.*;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.storage.shop.ShopRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EnableConfigurationProperties(ShopServiceConfigurationProperties.class)
class ShopServiceImpl implements ShopService {
    private final UUIDFactory uuidFactory;
    private final LocationService locationService;
    private final ShopRepository shopRepository;
    private final EmailService emailService;
    private final ShopLoginService shopLoginService;
    private final Clock clock;
    private final ShopServiceConfigurationProperties config;

    @Override
    @Transactional(readOnly = true)
    public List<Shop> listAll() {
        return shopRepository.listAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopWithDistance> findNearby(String zipCode) {
        GeoLocation location = locationService.lookup(zipCode);
        return shopRepository.findNearby(location);
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
    @Transactional(readOnly = true)
    public Shop findByIdOrThrow(Shop.Id id) throws ShopNotFoundException {
        Shop shop = findById(id);
        if (shop == null) {
            throw new ShopNotFoundException(id);
        }
        return shop;
    }

    @Override
    @Transactional
    public Shop create(ShopCreation creation) throws ShopAlreadyExistsException {
        if (shopLoginService.hasLogin(creation.getEmail())) {
            throw new ShopAlreadyExistsException(creation.getEmail());
        }

        UUID id = uuidFactory.create();

        GeoLocation geoLocation = locationService.lookup(creation.getZipCode());
        Shop shop = new Shop(
            Shop.Id.of(id), creation.getName(), creation.getOwnerName(), creation.getEmail(), creation.getStreet(),
            creation.getZipCode(), creation.getCity(), creation.getAddressSupplement(), creation.getContactTypes(),
            config.isEnableShopsOnCreation(), geoLocation, creation.getDetails(), creation.getWebsite(), creation.getSlotConfig(), clock.nowZoned(),
            clock.nowZoned()
        );

        shopRepository.insert(shop);
        shopLoginService.createLogin(shop, creation.getEmail(), creation.getPassword());
        return shop;
    }

    @Override
    @Transactional
    public Shop update(Shop shop, ShopUpdate update) {
        GeoLocation geoLocation = locationService.lookup(update.getZipCode());

        Shop newShop = new Shop(
            shop.getId(), update.getName(), update.getOwnerName(), shop.getName(), update.getStreet(), update.getZipCode(),
            update.getCity(), update.getAddressSupplement(), update.getContactTypes(),
            shop.isEnabled(), geoLocation, shop.getDetails(), shop.getWebsite(), shop.getSlotConfig(), shop.getCreated(),
            clock.nowZoned()
        );

        shopRepository.update(newShop);
        return newShop;
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

    @Override
    @Transactional(readOnly = true)
    public List<ShopWithDistance> search(String query, String zipCode) {
        GeoLocation location = locationService.lookup(zipCode);
        return shopRepository.search(query, location);
    }
}
