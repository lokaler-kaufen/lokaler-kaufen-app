package de.qaware.mercury.mercury;

import de.qaware.mercury.mercury.business.login.AdminLoginService;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Map;

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
        Map<String, String> shops = Map.of(
            "Moe's Whiskey", "Moe",
            "Flo's Kaffeeladen", "Flo",
            "Vroni's Kleiderladen", "Vroni"
        );

        for (Map.Entry<String, String> shop : shops.entrySet()) {
            Shop createdShop = shopService.create(shop.getKey(), shop.getValue(), "Street 1", "12345", "Munich", "Hasenbergl");
            log.info("Created shop {}", createdShop);
        }
    }
}
