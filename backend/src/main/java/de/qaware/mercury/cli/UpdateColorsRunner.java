package de.qaware.mercury.cli;

import de.qaware.mercury.business.image.ImageNotFoundException;
import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This bean is run when the application is started with '--update-colors'
 */
@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class UpdateColorsRunner {
    private final ImageService imageService;
    private final ShopService shopService;
    private final Console console;

    public void run() {
        console.printLine("#######################");
        console.printLine("#   Updating colors   #");
        console.printLine("#######################");

        List<Shop> shopList = shopService.listAll();
        for (Shop shop : shopList) {
            if (shop.getImageId() != null) {
                try {
                    String color = imageService.getImageBackgroundColor(shop.getImageId());
                    shopService.updateShopColor(shop, color);
                } catch (ImageNotFoundException e) {
                    log.warn("Could not update color for shop with id {}.", shop.getId());
                    log.warn("Cause: ", e);
                }
            }
        }

        log.info("Finished updating {} shops", shopList.size());
    }
}
