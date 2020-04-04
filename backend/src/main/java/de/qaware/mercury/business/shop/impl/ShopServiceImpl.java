package de.qaware.mercury.business.shop.impl;

import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.admin.AdminService;
import de.qaware.mercury.business.email.EmailService;
import de.qaware.mercury.business.location.BoundingBox;
import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.location.LocationService;
import de.qaware.mercury.business.location.impl.DistanceUtil;
import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import de.qaware.mercury.business.login.ShopLoginService;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopAlreadyExistsException;
import de.qaware.mercury.business.shop.ShopCreation;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.business.shop.ShopWithDistance;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.image.Image;
import de.qaware.mercury.storage.shop.ShopRepository;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Null;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private final AdminService adminService;

    @Override
    @Transactional(readOnly = true)
    public List<Shop> listAll() {
        return shopRepository.listAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopWithDistance> findActive(String zipCode) throws LocationNotFoundException {
        GeoLocation location = locationService.lookup(zipCode);
        return toShopWithDistance(shopRepository.findActive(), location);
    }

    @Override
    public List<ShopWithDistance> findActive(String zipCode, int maxDistance) throws LocationNotFoundException {
        GeoLocation location = locationService.lookup(zipCode);
        BoundingBox searchArea = DistanceUtil.boundingBoxOf(location, maxDistance);
        List<Shop> shops = shopRepository.findActive(searchArea);
        List<ShopWithDistance> shopsWithDistance = toShopWithDistance(shops, location);
        return filterByDistance(shopsWithDistance, maxDistance);
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
    public Shop create(ShopCreation creation) throws ShopAlreadyExistsException, LocationNotFoundException {
        if (shopLoginService.hasLogin(creation.getEmail())) {
            throw new ShopAlreadyExistsException(creation.getEmail());
        }

        UUID id = uuidFactory.create();

        GeoLocation geoLocation = locationService.lookup(creation.getZipCode());
        Shop shop = new Shop(
            Shop.Id.of(id),
            creation.getName(),
            creation.getOwnerName(),
            creation.getEmail(),
            creation.getStreet(),
            creation.getZipCode(),
            creation.getCity(),
            creation.getAddressSupplement(),
            creation.getContacts(),
            true,
            config.isApproveShopsOnCreation(),
            null,
            geoLocation,
            creation.getDetails(),
            creation.getWebsite(),
            creation.getSlotConfig(),
            clock.nowZoned(),
            clock.nowZoned()
        );

        shopRepository.insert(shop);
        shopLoginService.createLogin(shop, creation.getEmail(), creation.getPassword());

        if (!shop.isApproved()) {
            // If the shop needs approval, send an email to the shop
            log.info("Sending shop created, awaiting approval email to {}", shop.getEmail());
            emailService.sendShopCreatedApprovalNeeded(shop);
            List<Admin> adminsToNotify = adminService.findAdminsToNotifyOnShopApprovalNeeded();
            emailService.sendAdminShopApprovalNeeded(adminsToNotify, shop);
        }

        return shop;
    }

    @Override
    @Transactional
    public Shop update(Shop shop, ShopUpdate update) throws LocationNotFoundException {
        GeoLocation geoLocation = locationService.lookup(update.getZipCode());

        Shop updatedShop = new Shop(
            shop.getId(),
            update.getName(),
            update.getOwnerName(),
            shop.getEmail(),
            update.getStreet(),
            update.getZipCode(),
            update.getCity(),
            update.getAddressSupplement(),
            update.getContacts(),
            shop.isEnabled(),
            shop.isApproved(),
            Null.map(update.getImageId(), Image.Id::parse),
            geoLocation,
            update.getDetails(),
            update.getWebsite(),
            update.getSlotConfig(),
            shop.getCreated(),
            clock.nowZoned()
        );

        shopRepository.update(updatedShop);
        return updatedShop;
    }

    @Override
    @Transactional
    public void changeApproved(Shop.Id id, boolean approved) throws ShopNotFoundException {
        Shop shop = shopRepository.findById(id);
        if (shop == null) {
            throw new ShopNotFoundException(id);
        }
        if (shop.isApproved() == approved) {
            // Nothing to do, state in database is the same as the new state
            return;
        }

        Shop updatedShop = shop.withApproved(approved);
        shopRepository.update(updatedShop);

        if (approved) {
            // Shop has been approved
            log.info("Shop '{}' has been approved", shop.getId());
            emailService.sendShopApproved(shop);
        } else {
            // Shop was approved, has been disapproved
            log.info("Shop '{}' has been disapproved", shop.getId());
            emailService.sendShopApprovalRevoked(shop);
        }
    }


    @Override
    public void addImage(Shop.Id id, Image.Id imageId) throws ShopNotFoundException {
        Shop shop = findByIdOrThrow(id);
        Shop updatedShop = shop.withImageId(imageId);
        shopRepository.update(updatedShop);
    }

    @Override
    @Transactional(readOnly = true)
    public void sendCreateLink(String email) throws ShopAlreadyExistsException {
        log.info("Sending shop creation link to '{}'", email);

        if (shopLoginService.hasLogin(email)) {
            throw new ShopAlreadyExistsException(email);
        }

        emailService.sendShopCreationLink(email);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Shop> findByName(String name) {
        return shopRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopWithDistance> searchActive(String query, String zipCode) throws LocationNotFoundException {
        GeoLocation location = locationService.lookup(zipCode);
        List<Shop> shops = shopRepository.searchActive(query);
        return toShopWithDistance(shops, location);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ShopWithDistance> searchActive(String query, String zipCode, int maxDistance) throws LocationNotFoundException {
        GeoLocation location = locationService.lookup(zipCode);
        BoundingBox searchArea = DistanceUtil.boundingBoxOf(location, maxDistance);
        List<Shop> shops = shopRepository.searchActive(query, searchArea);
        List<ShopWithDistance> shopsWithDistance = toShopWithDistance(shops, location);
        return filterByDistance(shopsWithDistance, maxDistance);
    }

    private List<ShopWithDistance> toShopWithDistance(List<Shop> shops, GeoLocation location) {
        return Lists.map(shops, s -> new ShopWithDistance(s, DistanceUtil.distanceInKmBetween(s.getGeoLocation(), location)
        ));
    }

    private List<ShopWithDistance> filterByDistance(List<ShopWithDistance> shops, int maxDistance) {
        return shops.stream().filter(s -> s.getDistance() <= maxDistance).collect(Collectors.toList());
    }

}
