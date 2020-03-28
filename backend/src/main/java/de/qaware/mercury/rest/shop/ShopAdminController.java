package de.qaware.mercury.rest.shop;

import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper;
import de.qaware.mercury.rest.shop.dto.both.ContactDto;
import de.qaware.mercury.rest.shop.dto.request.UpdateShopDto;
import de.qaware.mercury.rest.shop.dto.response.ShopAdminDto;
import de.qaware.mercury.rest.shop.dto.response.ShopsAdminDto;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.validation.Validation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@RestController
@RequestMapping(value = "/api/admin/shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Validated
class ShopAdminController {
    private final ShopService shopService;
    private final AuthenticationHelper authenticationHelper;

    /**
     * Retrieves settings for a shop.
     *
     * @param id             the id of the shop.
     * @param servletRequest an instance of {@link HttpServletRequest}.
     * @return an instance of {@link ShopAdminDto}.
     * @throws ShopNotFoundException if the shop cannot be found in the database.
     * @throws LoginException        if the caller is not authenticated as admin.
     */
    @GetMapping
    public ShopAdminDto getShopSettings(@PathVariable @Pattern(regexp = Validation.SHOP_ID) String id, HttpServletRequest servletRequest) throws ShopNotFoundException, LoginException {
        authenticationHelper.authenticateAdmin(servletRequest);
        Shop shop = shopService.findById(Shop.Id.parse(id));
        if (shop == null) {
            throw new ShopNotFoundException(Shop.Id.parse(id));
        }

        return ShopAdminDto.of(shop);
    }

    @GetMapping
    public ShopsAdminDto listAll(HttpServletRequest request) throws LoginException {
        authenticationHelper.authenticateAdmin(request);

        return ShopsAdminDto.of(shopService.listAll());
    }

    @PutMapping(path = "/{id}/approve")
    public void changeApprove(@PathVariable @Pattern(regexp = Validation.SHOP_ID) String id, @RequestParam boolean approved, HttpServletRequest request) throws ShopNotFoundException, LoginException {
        authenticationHelper.authenticateAdmin(request);
        Shop.Id shopId = Shop.Id.parse(id);

        shopService.changeApproved(shopId, approved);
    }

    @PutMapping(path = "/{id}")
    public ShopAdminDto update(@PathVariable @Pattern(regexp = Validation.SHOP_ID) String id, @Valid @RequestBody UpdateShopDto request, HttpServletRequest servletRequest) throws ShopNotFoundException, LoginException {
        Admin admin = authenticationHelper.authenticateAdmin(servletRequest);
        Shop.Id shopId = Shop.Id.parse(id);

        Shop shop = shopService.findById(shopId);
        if (shop == null) {
            throw new ShopNotFoundException(shopId);
        }

        log.info("Admin {} updated shop '{}'", admin.getEmail(), shop.getName());
        return ShopAdminDto.of(shopService.update(shop, new ShopUpdate(
            request.getName(),
            request.getOwnerName(),
            request.getStreet(),
            request.getZipCode(),
            request.getCity(),
            request.getAddressSupplement(),
            request.getDetails(),
            request.getWebsite(),
            Lists.map(request.getContacts(), ContactDto::toContact),
            request.getSlots().toSlots()
        )));
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable @Pattern(regexp = Validation.SHOP_ID) String id, HttpServletRequest request) throws ShopNotFoundException, LoginException {
        Admin admin = authenticationHelper.authenticateAdmin(request);
        Shop.Id shopId = Shop.Id.parse(id);

        log.info("Admin {} deleted shop {}", admin.getEmail(), shopId);
        shopService.delete(shopId);
    }
}
