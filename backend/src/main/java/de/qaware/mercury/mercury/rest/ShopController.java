package de.qaware.mercury.mercury.rest;

import de.qaware.mercury.mercury.business.ShopService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/shop", produces = MediaType.APPLICATION_JSON_VALUE)
class ShopController {
    private final ShopService shopService;

    ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    ShopsDto listAll() {
        return ShopsDto.of(shopService.listAll());
    }
}
