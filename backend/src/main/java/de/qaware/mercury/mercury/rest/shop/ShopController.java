package de.qaware.mercury.mercury.rest.shop;

import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.login.PasswordResetToken;
import de.qaware.mercury.mercury.business.login.ShopCreationToken;
import de.qaware.mercury.mercury.business.login.ShopLoginNotFoundException;
import de.qaware.mercury.mercury.business.login.ShopLoginService;
import de.qaware.mercury.mercury.business.login.TokenService;
import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopAlreadyExistsException;
import de.qaware.mercury.mercury.business.shop.ShopCreation;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.mercury.rest.plumbing.authentication.AuthenticationHelper;
import de.qaware.mercury.mercury.rest.shop.dto.request.CreateShopDto;
import de.qaware.mercury.mercury.rest.shop.dto.request.ResetPasswordDto;
import de.qaware.mercury.mercury.rest.shop.dto.request.SendCreateLinkDto;
import de.qaware.mercury.mercury.rest.shop.dto.request.SendPasswordResetLinkDto;
import de.qaware.mercury.mercury.rest.shop.dto.request.UpdateShopDto;
import de.qaware.mercury.mercury.rest.shop.dto.response.ShopDetailDto;
import de.qaware.mercury.mercury.rest.shop.dto.response.ShopListDto;
import de.qaware.mercury.mercury.rest.shop.dto.response.ShopOwnerDetailDto;
import de.qaware.mercury.mercury.util.Maps;
import de.qaware.mercury.mercury.util.validation.GuidValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
class ShopController {
    private final ShopService shopService;
    private final TokenService tokenService;
    private final AuthenticationHelper authenticationHelper;
    private final ShopLoginService shopLoginService;

    @GetMapping(path = "/{id}")
    public ShopDetailDto getDetails(@PathVariable @Pattern(regexp = GuidValidation.REGEX) String id) throws ShopNotFoundException {
        Shop shop = shopService.findById(Shop.Id.parse(id));
        if (shop == null) {
            throw new ShopNotFoundException(Shop.Id.parse(id));
        }

        return ShopDetailDto.of(shop);
    }

    @GetMapping(path = "/me")
    public ShopOwnerDetailDto getDetails(HttpServletRequest request) throws LoginException {
        Shop shop = authenticationHelper.authenticateShop(request);

        return ShopOwnerDetailDto.of(shop);
    }

    @PostMapping(path = "/send-create-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendCreateLink(@Valid @RequestBody SendCreateLinkDto request) {
        shopService.sendCreateLink(request.getEmail());
    }


    @PostMapping(path = "/send-password-reset-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendPasswordResetLink(@Valid @RequestBody SendPasswordResetLinkDto request) {
        shopLoginService.sendPasswordResetLink(request.getEmail());
    }

    @PostMapping(path = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void resetPassword(@Valid @RequestBody ResetPasswordDto request, @RequestParam @NotBlank String token) throws LoginException, ShopLoginNotFoundException {
        String email = tokenService.verifyPasswordResetToken(PasswordResetToken.of(token));

        shopLoginService.resetPassword(email, request.getPassword());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ShopDetailDto createShop(@Valid @RequestBody CreateShopDto request, @RequestParam @NotBlank String token) throws LoginException, ShopAlreadyExistsException {
        // The token is taken from the link which the user got with email
        // It contains the email address, and is used to verify that the user really has access to this email address
        String email = tokenService.verifyShopCreationToken(ShopCreationToken.of(token));

        return ShopDetailDto.of(shopService.create(new ShopCreation(
            email, request.getOwnerName(), request.getName(), request.getStreet(), request.getZipCode(), request.getCity(),
            request.getAddressSupplement(), request.getDetails(), request.getWebsite(), request.getPassword(),
            Maps.mapKeys(request.getContactTypes(), ContactType::parse), request.getSlots().toSlots()
        )));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ShopDetailDto updateShop(@Valid @RequestBody UpdateShopDto request, HttpServletRequest servletRequest) throws LoginException {
        Shop shop = authenticationHelper.authenticateShop(servletRequest);

        return ShopDetailDto.of(shopService.update(shop, new ShopUpdate(
            request.getName(), request.getOwnerName(), request.getStreet(), request.getZipCode(), request.getCity(),
            request.getAddressSupplement(), request.getDetails(), request.getWebsite(),
            Maps.mapKeys(request.getContactTypes(), ContactType::parse), request.getSlots().toSlots()
        )));
    }

    @GetMapping("nearby")
    public ShopListDto listNearby(@RequestParam @NotBlank String location) {
        return ShopListDto.of(shopService.findNearby(location));
    }

    @GetMapping("search")
    public ShopListDto listNearby(@RequestParam @NotBlank String query, @NotBlank @RequestParam String location) {
        return ShopListDto.of(shopService.search(query, location));
    }
}
