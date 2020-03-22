package de.qaware.mercury.mercury;

import de.qaware.mercury.mercury.business.location.GeoLocationLookup;
import de.qaware.mercury.mercury.business.location.GeoLocationSuggestion;
import de.qaware.mercury.mercury.business.login.AdminLoginService;
import de.qaware.mercury.mercury.business.shop.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class DebugPopulateDatabase implements ApplicationRunner {
    private final ShopService shopService;
    private final GeoLocationLookup geoLocationLookup;
    private final AdminLoginService adminLoginService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("Populating database for DEBUG purposes");

        createAdmins();
        createShops();

        shopService.findNearby("01998");
        String term = "Rosenh";
        List<GeoLocationSuggestion> locations = geoLocationLookup.search(term);
        log.info("Search for location '{}' returned:", term);
        locations.forEach(l -> log.info(l.toString()));
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

    private void createShops() {
        createShop(new ShopCreation(
            "moe@localhost", "Moe", "Moe's Whiskey", "Lothstr. 64", "85579", "Neubiberg", "", "Bester Whiskey in ganz Neubiberg!",
            "https://www.moes-whiskey.com/", "moe",
            Map.of(ContactType.WHATSAPP, "0151/123456789", ContactType.FACEBOOK_MESSENGER, "moe@localhost"), SlotConfig.none(15, 5)
        ));
        createShop(new ShopCreation(
            "flo@localhost", "Flo", "Flo's Kaffeeladen", "Aschauer Str. 32", "81549", "München", "", "", null,
            "flo", Map.of(ContactType.GLIDE, "@vlow"), SlotConfig.none(30, 10)
        ));
        createShop(new ShopCreation(
            "vroni@localhost", "Vroni", "Vroni's Kleiderladen", "Rheinstraße 4C", "55116", "Mainz", "", "", null,
            "vroni", Map.of(ContactType.GOOGLE_DUO, "vroni@localhost", ContactType.TANGO, "@vroni"), SlotConfig.none(60, 15)
        ));
    }

    private void createShop(ShopCreation creation) {
        if (shopService.findByName(creation.getName()).isEmpty()) {
            Shop shop = shopService.create(creation);
            log.info("Created shop {}", shop);
        }
    }
}
