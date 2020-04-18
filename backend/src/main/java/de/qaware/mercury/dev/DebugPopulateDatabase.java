package de.qaware.mercury.dev;

import de.qaware.mercury.business.image.Image;
import de.qaware.mercury.business.image.ImageService;
import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import de.qaware.mercury.business.login.AdminEmailSettings;
import de.qaware.mercury.business.login.AdminLoginService;
import de.qaware.mercury.business.shop.Breaks;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopAlreadyExistsException;
import de.qaware.mercury.business.shop.ShopCreation;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.business.shop.SocialLinks;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@ConditionalOnProperty(name = "mercury.features.enable-debug-data-creation", havingValue = "true")
class DebugPopulateDatabase implements ApplicationRunner {
    private final ShopService shopService;
    private final AdminLoginService adminLoginService;
    private final ImageService imageService;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("Populating database for DEBUG purposes");

        createAdmins();
        createShops();
    }

    private void createAdmins() {
        createAdmin("admin-1@local.host", "admin-1");
        createAdmin("admin-2@local.host", "admin-2");
    }

    private void createAdmin(String email, String password) {
        if (adminLoginService.findByEmail(email) == null) {
            adminLoginService.createLogin(email, password, new AdminEmailSettings(false));
        }
    }

    private void createShops() throws ShopNotFoundException, ShopAlreadyExistsException, LocationNotFoundException, IOException {
        createShop(new ShopCreation(
            "moe@local.host", "Moe", "Moe's Whiskey", "Lothstr. 64", "85579", "Neubiberg", "", "Bester Whiskey in ganz Neubiberg!",
            "https://www.moes-whiskey.com/", true, "moe",
            Map.of(ContactType.WHATSAPP, "0151/123456789", ContactType.FACETIME, "moe@local.host"),
            new SlotConfig(15, 5, 60,
                new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                new DayConfig(LocalTime.of(8, 0), LocalTime.of(17, 0))
            ), new SocialLinks("dermoe", "der.moe", "@dermoe"), Breaks.everyday(LocalTime.of(12, 0), LocalTime.of(13, 0))
        ), "/dev/shopimages/moe.jpg");

        createShop(new ShopCreation(
            "flo@local.host", "Flo", "Flo's Kaffeeladen", "Aschauer Str. 32", "81549", "München", "", "Hier gibts jede Art von Kaffee!", null,
            true, "flo", Map.of(ContactType.GOOGLE_DUO, "@vlow"),
            new SlotConfig(30, 10, 45, null, null, null, null, null, null, null),
            new SocialLinks("derflo", "der.flo", "@derflo"), Breaks.everyday(LocalTime.of(11, 30), LocalTime.of(12, 0))
        ), "/dev/shopimages/flo.jpg");

        createShop(new ShopCreation(
            "vroni@local.host", "Vroni", "Vroni's Kleiderladen", "Rheinstraße 4C", "55116", "Mainz", "", "Kleider, Kleider, Kleider!", null,
            true, "vroni", Map.of(ContactType.GOOGLE_DUO, "vroni@local.host", ContactType.SIGNAL, "@vroni"),
            new SlotConfig(60, 15, 120, null, null, null, null, null, null, null),
            new SocialLinks("dievroni", "die.vroni", "@dievroni"), Breaks.everyday(LocalTime.of(12, 0), LocalTime.of(14, 0))
        ), "/dev/shopimages/vroni.jpg");
    }

    private void createShop(ShopCreation creation, String imageResource) throws ShopNotFoundException, ShopAlreadyExistsException, LocationNotFoundException, IOException {
        if (shopService.findByName(creation.getName()).isEmpty()) {
            Shop shop = shopService.create(creation);
            shopService.changeApproved(shop.getId(), true);

            Image image;
            try (InputStream stream = DebugPopulateDatabase.class.getResourceAsStream(imageResource)) {
                Objects.requireNonNull(stream, String.format("Can't load resource '%s'", imageResource));
                image = imageService.addImage(shop.getId(), stream);
            }
            shopService.setImage(shop, image, null);

            log.info("Created shop {}", shop);
        }
    }
}
