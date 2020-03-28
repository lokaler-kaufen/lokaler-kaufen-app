package de.qaware.mercury;

import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import de.qaware.mercury.business.login.AdminLoginService;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopAlreadyExistsException;
import de.qaware.mercury.business.shop.ShopCreation;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.business.shop.SlotConfig;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ConditionalOnProperty(name = "mercury.features.enable-debug-data-creation", havingValue = "true")
class DebugPopulateDatabase implements ApplicationRunner {
    private final ShopService shopService;
    private final AdminLoginService adminLoginService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("Populating database for DEBUG purposes");

        createAdmins();
        createShops();
    }

    private void createAdmins() {
        createAdmin("admin-1@localhost", "admin-1");
        createAdmin("admin-2@localhost", "admin-2");
    }

    private void createAdmin(String email, String password) {
        if (adminLoginService.findByEmail(email) == null) {
            adminLoginService.createLogin(email, password);
        }
    }

    private void createShops() throws ShopNotFoundException, ShopAlreadyExistsException, LocationNotFoundException {
        createShop(new ShopCreation(
            "moe@localhost", "Moe", "Moe's Whiskey", "Lothstr. 64", "85579", "Neubiberg", "", "Bester Whiskey in ganz Neubiberg!",
            "https://www.moes-whiskey.com/", "moe",
            Map.of(ContactType.WHATSAPP, "0151/123456789", ContactType.FACETIME, "moe@localhost"),
            SlotConfig.builder().timePerSlot(15).timeBetweenSlots(5)
                .monday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)))
                .tuesday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)))
                .wednesday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)))
                .thursday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)))
                .friday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)))
                .saturday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)))
                .sunday(new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)))
                .build()
        ));
//        createShop(new ShopCreation(
//            "flo@localhost", "Flo", "Flo's Kaffeeladen", "Aschauer Str. 32", "81549", "München", "", "", null,
//            "flo", Map.of(ContactType.GOOGLE_DUO, "@vlow"),
//            SlotConfig.builder().timePerSlot(30).timeBetweenSlots(10).build()
//        ));
//        createShop(new ShopCreation(
//            "vroni@localhost", "Vroni", "Vroni's Kleiderladen", "Rheinstraße 4C", "55116", "Mainz", "", "", null,
//            "vroni", Map.of(ContactType.GOOGLE_DUO, "vroni@localhost", ContactType.SIGNAL, "@vroni"),
//            SlotConfig.builder().timePerSlot(60).timeBetweenSlots(15).build()
//        ));
    }

    private void createShop(ShopCreation creation) throws ShopNotFoundException, ShopAlreadyExistsException, LocationNotFoundException {
        if (shopService.findByName(creation.getName()).isEmpty()) {
            Shop shop = shopService.create(creation);
            shopService.changeApproved(shop.getId(), true);
            log.info("Created shop {}", shop);

            Map<ContactType, String> newContacts = new HashMap<>();
            newContacts.put(ContactType.FACETIME, "foobar");

            shopService.update(shop, new ShopUpdate(
                shop.getName(), shop.getOwnerName(), shop.getStreet(), shop.getZipCode(), shop.getCity(), shop.getAddressSupplement(),
                shop.getDetails(), shop.getWebsite(), newContacts, shop.getSlotConfig()
            ));
        }
    }
}
