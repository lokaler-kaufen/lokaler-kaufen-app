package de.qaware.mercury.mercury.rest.shop;

import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.rest.shop.dto.ShopsDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin", produces = MediaType.APPLICATION_JSON_VALUE)
class AdminController {
    private final ShopService shopService;

    AdminController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    ShopsDto listAll() {
        return ShopsDto.of(shopService.listAll());
    }

    @PutMapping(path = "/availability")
    void setAvailability(@RequestParam String shopId, @RequestParam boolean enabled) {
        shopService.setAvailability(shopId, enabled);
    }
}
