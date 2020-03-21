package de.qaware.mercury.mercury.rest.shop;

import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.mercury.rest.ErrorDto;
import de.qaware.mercury.mercury.rest.shop.dto.ShopsAdminDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/admin/shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
class ShopAdminController {
    private final ShopService shopService;
    private final UUIDFactory uuidFactory;

    ShopAdminController(ShopService shopService, UUIDFactory uuidFactory) {
        this.shopService = shopService;
        this.uuidFactory = uuidFactory;
    }

    @GetMapping
    ShopsAdminDto listAll() {
        return ShopsAdminDto.of(shopService.listAll());
    }

    @PutMapping(path = "/{id}/enable")
    void changeEnabled(@PathVariable String id, @RequestParam boolean enabled) throws ShopNotFoundException {
        shopService.changeEnabled(Shop.Id.parse(id), enabled);
    }

    @DeleteMapping(path = "/{id}/delete")
    void delete(@PathVariable String id) throws ShopNotFoundException {
        shopService.delete(Shop.Id.parse(id));
    }

    @ExceptionHandler(ShopNotFoundException.class)
    ResponseEntity<ErrorDto> handleShopNotFoundException(ShopNotFoundException exception) {
        String exceptionId = uuidFactory.create().toString();
        log.debug("Handled ShopNotFoundException with exception id {}", exceptionId);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(
            exceptionId, "SHOP_NOT_FOUND", exception.getMessage()
        ));
    }
}
