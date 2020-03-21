package de.qaware.mercury.mercury.rest.shop;

import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.login.ShopCreationToken;
import de.qaware.mercury.mercury.business.login.TokenService;
import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopCreation;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.mercury.business.shop.Slots;
import de.qaware.mercury.mercury.rest.plumbing.authentication.AuthenticationHelper;
import de.qaware.mercury.mercury.rest.plumbing.authentication.InvalidCredentialsRestException;
import de.qaware.mercury.mercury.rest.shop.dto.CreateShopRequestDto;
import de.qaware.mercury.mercury.rest.shop.dto.SendCreateLinkDto;
import de.qaware.mercury.mercury.rest.shop.dto.ShopDetailDto;
import de.qaware.mercury.mercury.rest.shop.dto.ShopListDto;
import de.qaware.mercury.mercury.rest.shop.dto.UpdateShopRequestDto;
import de.qaware.mercury.mercury.util.Maps;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/shop", produces = MediaType.APPLICATION_JSON_VALUE)
class ShopController {
    private final ShopService shopService;
    private final TokenService tokenService;
    private final AuthenticationHelper authenticationHelper;

    @GetMapping(path = "/{id}")
    ShopDetailDto getDetails(@PathVariable String id) throws ShopNotFoundException {
        Shop shop = shopService.findById(Shop.Id.parse(id));
        if (shop == null) {
            throw new ShopNotFoundException(Shop.Id.parse(id));
        }

        return ShopDetailDto.of(shop);
    }

    @PostMapping(path = "/send-create-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    void sendCreateLink(@RequestBody SendCreateLinkDto request) {
        shopService.sendCreateLink(request.getEmail());
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    ShopDetailDto createShop(@RequestBody CreateShopRequestDto request, @RequestParam String token) {
        // The token is taken from the link which the user got with email
        // It contains the email address, and is used to verify that the user really has access to this email address
        String email;
        try {
            email = tokenService.verifyShopCreationToken(ShopCreationToken.of(token));
        } catch (LoginException e) {
            throw new InvalidCredentialsRestException(e.getMessage());
        }

        return ShopDetailDto.of(shopService.create(new ShopCreation(
            email, request.getOwnerName(), request.getName(), request.getStreet(), request.getZipCode(), request.getCity(),
            request.getAddressSupplement(), request.getDetails(), request.getWebsite(), request.getPassword(),
            Maps.mapKeys(request.getContactTypes(), ContactType::valueOf),
            // TODO MKA: Slots
            Slots.none(request.getSlots().getTimePerSlot(), request.getSlots().getTimeBetweenSlots())
        )));
    }

    @PutMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    ShopDetailDto updateShop(@RequestBody UpdateShopRequestDto request, HttpServletRequest servletRequest) {
        Shop shop = authenticationHelper.authenticateShop(servletRequest);

        return ShopDetailDto.of(shopService.update(shop, new ShopUpdate(
            request.getName(), request.getOwnerName(), request.getStreet(), request.getZipCode(), request.getCity(),
            request.getAddressSupplement(), request.getDetails(), request.getWebsite(),
            Maps.mapKeys(request.getContactTypes(), ContactType::valueOf),
            // TODO MKA: Slots
            Slots.none(request.getSlots().getTimePerSlot(), request.getSlots().getTimeBetweenSlots())
        )));
    }

    @GetMapping("nearby")
    ShopListDto listNearby(@RequestParam String location) {
        return ShopListDto.of(shopService.findNearby(location));
    }

    @GetMapping("search")
    ShopListDto listNearby(@RequestParam String query, @RequestParam String location) {
        return ShopListDto.of(shopService.search(query, location));
    }

}
