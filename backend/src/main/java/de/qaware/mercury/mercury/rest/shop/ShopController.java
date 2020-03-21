package de.qaware.mercury.mercury.rest.shop;

import de.qaware.mercury.mercury.business.shop.ShopService;
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
@RequestMapping(value = "/api/shop", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
class ShopController {
    private final ShopService shopService;

    ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @PostMapping("/create")
    ShopCreateDto createShop(@RequestBody ShopCreateDto shop) {
        return ShopCreateDto.of(shopService.create(
            shop.getName(),
            shop.getOwnerName(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement()
        ));
    }

    @GetMapping("find")
    ShopsDto findNearby(@RequestParam String location) {
        return ShopsDto.of(shopService.findNearby(location));
    }
}
