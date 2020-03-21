package de.qaware.mercury.mercury;

import de.qaware.mercury.mercury.business.login.AdminLoginService;
import de.qaware.mercury.mercury.business.login.ShopLoginService;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
class DebugPopulateDatabase implements ApplicationRunner {
    private final ShopService shopService;
    private final AdminLoginService adminLoginService;
    private final ShopLoginService shopLoginService;

    DebugPopulateDatabase(ShopService shopService, AdminLoginService adminLoginService, ShopLoginService shopLoginService) {
        this.shopService = shopService;
        this.adminLoginService = adminLoginService;
        this.shopLoginService = shopLoginService;
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
        Shop shop = shopService.create("Moe's Whiskey", "Moe", "Lothstr. 64", "80335", "München", "");
        shopLoginService.createLogin(shop, "moe@localhost", "moe");
        log.info("Created shop {}", shop);

        shop = shopService.create("Flo's Kaffeeladen", "Flo", "Aschauer Str. 32", "81549", "München", "");
        shopLoginService.createLogin(shop, "flo@localhost", "flo");
        log.info("Created shop {}", shop);

        shop = shopService.create("Vroni's Kleiderladen", "Vroni", "Rheinstraße 4C", "55116", "Mainz", "");
        shopLoginService.createLogin(shop, "vroni@localhost", "vroni");
        log.info("Created shop {}", shop);
    }
}
