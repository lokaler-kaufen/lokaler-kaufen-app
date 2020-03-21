package de.qaware.mercury.mercury.rest.shop;

import de.qaware.mercury.mercury.business.admin.Admin;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.business.uuid.UUIDFactory;
import de.qaware.mercury.mercury.rest.ErrorDto;
import de.qaware.mercury.mercury.rest.plumbing.authentication.AuthenticationHelper;
import de.qaware.mercury.mercury.rest.shop.dto.ShopAdminDto;
import de.qaware.mercury.mercury.rest.shop.dto.ShopsAdminDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/admin/shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ShopAdminController {
    private final ShopService shopService;
    private final UUIDFactory uuidFactory;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping
    ShopsAdminDto listAll(HttpServletRequest request) {
        authenticationHelper.authenticateAdmin(request);

        return ShopsAdminDto.of(shopService.listAll());
    }

    @PutMapping(path = "/{id}/enable")
    void changeEnabled(@PathVariable String id, @RequestParam boolean enabled, HttpServletRequest request) throws ShopNotFoundException {
        Admin admin = authenticationHelper.authenticateAdmin(request);
        Shop.Id shopId = Shop.Id.parse(id);

        log.info("Admin '{}' changed enabled flag from shop {} to {}", admin.getEmail(), shopId, enabled);
        shopService.changeEnabled(shopId, enabled);
    }

    @PutMapping(path = "/{id}")
    ShopAdminDto update(@PathVariable String id, @RequestBody ShopAdminDto shop, HttpServletRequest request) {
        Admin admin = authenticationHelper.authenticateAdmin(request);
        Shop.Id shopId = Shop.Id.parse(id);

        log.info("Admin {} updated shop {}", admin.getEmail(), shopId);
        return ShopAdminDto.of(shopService.update(
            shopId,
            shop.getName(),
            shop.getOwnerName(),
            shop.getEmail(), // Email is not taken from request body, but from the token which was in the email link
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            shop.getContactTypes(),
            shop.isEnabled()
        ));
    }

    @DeleteMapping(path = "/{id}")
    void delete(@PathVariable String id, HttpServletRequest request) throws ShopNotFoundException {
        Admin admin = authenticationHelper.authenticateAdmin(request);
        Shop.Id shopId = Shop.Id.parse(id);

        log.info("Admin {} deleted shop {}", admin.getEmail(), shopId);
        shopService.delete(shopId);
    }

    @ExceptionHandler(ShopNotFoundException.class)
    ResponseEntity<ErrorDto> handleShopNotFoundException(ShopNotFoundException exception) {
        ErrorDto errorDto = ErrorDto.of(uuidFactory, "SHOP_NOT_FOUND", exception.getMessage());
        log.debug("Handled ShopNotFoundException with exception id {}", errorDto.getId());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDto);
    }
}
