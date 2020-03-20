package de.qaware.mercury.mercury;

import de.qaware.mercury.mercury.business.Shop;
import de.qaware.mercury.mercury.business.ShopService;
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
        Set<String> names = Set.of(
            "Moe's Whiskey",
            "Flo's Kaffeeladen",
            "Vroni's Kleiderladen"
        );

        for (String name : names) {
            Shop shop = shopService.create(name);
            log.info("Created shop {}", shop);
        }
    }
}
