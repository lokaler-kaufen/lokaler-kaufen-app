package de.qaware.mercury.mercury.rest.shop;

import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.rest.shop.dto.SendCreateLinkDto;
import de.qaware.mercury.mercury.rest.shop.dto.ShopCreateDto;
import de.qaware.mercury.mercury.rest.shop.dto.ShopsDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/shop", produces = MediaType.APPLICATION_JSON_VALUE)
class ShopController {
    private final ShopService shopService;

    ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping(path = "/send-create-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendCreateLink(@RequestBody SendCreateLinkDto request) {
        shopService.sendCreateLink(request.getEmail());
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    ShopCreateDto createShop(@RequestBody ShopCreateDto shop) {
        return ShopCreateDto.of(shopService.create(
            shop.getName(),
            shop.getOwnerName(),
            shop.getEmail(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            shop.getContactTypes()
        ));
    }

    @GetMapping("find")
    ShopsDto findNearby(@RequestParam String location) {
        return ShopsDto.of(shopService.findNearby(location));
    }
}
