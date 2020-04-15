package de.qaware.mercury.rest.shop;

import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.shop.ShopSharingConfigurationProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@Validated
@RestController
@RequestMapping("/api/shop/redirect")
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@EnableConfigurationProperties(ShopSharingConfigurationProperties.class)
public class ShopRedirectController {
    private final ShopService shopService;
    private final ShopSharingConfigurationProperties configuration;

    @GetMapping("/{slug}")
    public ResponseEntity<Void> redirect(@PathVariable("slug") @NotBlank String slug) throws ShopNotFoundException {
        Shop shop = shopService.findBySlug(slug);
        if (shop == null) {
            throw ShopNotFoundException.ofSlug(slug);
        }

        String url = configuration.getShopDetailLinkTemplate()
            .replace("{{ id }}", shop.getId().getId().toString());

        // See https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/301
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", url);
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }
}
