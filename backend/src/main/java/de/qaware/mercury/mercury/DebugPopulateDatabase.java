package de.qaware.mercury.mercury;

import de.qaware.mercury.mercury.business.location.Location;
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

    DebugPopulateDatabase(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Populating database for DEBUG purposes");

        Set<String> names = Set.of(
            "Moe's Whiskey",
            "Flo's Kaffeeladen",
            "Vroni's Kleiderladen"
        );

        for (String name : names) {
            Shop shop1 = shopService.create(name + " München", "81549", true);
            Shop shop2 = shopService.create(name + " Rosenheim", new Location("83022 Rosenheim", 47.848671, 12.139321), true);
            log.info("Created shop {}", shop1);
            log.info("Created shop {}", shop2);
        }
    }
}
