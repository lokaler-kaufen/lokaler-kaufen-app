package de.qaware.mercury.mercury.rest.shop;

import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.rest.shop.dto.ShopsDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("find")
    ShopsDto findNearby(@RequestParam String location) {
        return ShopsDto.of(shopService.findNearby(location));
    }
}
