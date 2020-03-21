package de.qaware.mercury.mercury;

import de.qaware.mercury.mercury.business.login.AdminLoginService;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
class DebugPopulateDatabase implements ApplicationRunner {
    private final ShopService shopService;
    private final AdminLoginService adminLoginService;

    DebugPopulateDatabase(ShopService shopService, AdminLoginService adminLoginService) {
        this.shopService = shopService;
        this.adminLoginService = adminLoginService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Populating database for DEBUG purposes");

        createAdmins();
        createShops();
    }

    private void createAdmins() {
        adminLoginService.createLogin("admin-1@localhost", "admin-1");
        adminLoginService.createLogin("admin-2@localhost", "admin-2");
    }

    private void createShops() {
        Set<String> names = Set.of(
            "Moe's Whiskey",
            "Flo's Kaffeeladen",
            "Vroni's Kleiderladen"
        );

        for (String name : names) {
            Shop shop = shopService.create(name, "Street 1", "12345", "Munich");
            log.info("Created shop {}", shop);
        }
    }
}
