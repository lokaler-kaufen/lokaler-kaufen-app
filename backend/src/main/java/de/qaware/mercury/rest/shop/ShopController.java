package de.qaware.mercury.rest.shop;

import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.PasswordResetToken;
import de.qaware.mercury.business.login.ShopCreationToken;
import de.qaware.mercury.business.login.ShopLoginNotFoundException;
import de.qaware.mercury.business.login.ShopLoginService;
import de.qaware.mercury.business.login.TokenService;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopAlreadyExistsException;
import de.qaware.mercury.business.shop.ShopCreation;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.shop.ShopUpdate;
import de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper;
import de.qaware.mercury.rest.shop.dto.both.ContactDto;
import de.qaware.mercury.rest.shop.dto.request.CreateContactDto;
import de.qaware.mercury.rest.shop.dto.request.CreateShopDto;
import de.qaware.mercury.rest.shop.dto.request.ResetPasswordDto;
import de.qaware.mercury.rest.shop.dto.request.SendCreateLinkDto;
import de.qaware.mercury.rest.shop.dto.request.SendPasswordResetLinkDto;
import de.qaware.mercury.rest.shop.dto.request.UpdateShopDto;
import de.qaware.mercury.rest.shop.dto.response.ShopDetailDto;
import de.qaware.mercury.rest.shop.dto.response.ShopListDto;
import de.qaware.mercury.rest.shop.dto.response.ShopOwnerDetailDto;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.validation.Validation;
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

/**
 * This controller is used to retrieve shop DTOs for users and shop owners.
 */
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

    /**
     * Retrieves details of a shop for users.
     *
     * @param id the id of the shop.
     * @return an instance of {@link ShopDetailDto}.
     * @throws ShopNotFoundException if no shop with the corresponding id was found.
     */
    @GetMapping(path = "/{id}")
    public ShopDetailDto getShopDetails(@PathVariable @Pattern(regexp = Validation.SHOP_ID) String id) throws ShopNotFoundException {
        Shop shop = shopService.findById(Shop.Id.parse(id));
        if (shop == null) {
            throw new ShopNotFoundException(Shop.Id.parse(id));
        }

        return ShopDetailDto.of(shop);
    }

    /**
     * Retrieves settings of a shop for the owner.
     *
     * @param servletRequest an instance of {@link HttpServletRequest}.
     * @return an instance of {@link ShopOwnerDetailDto}.
     * @throws LoginException if the caller is not authenticated as a shop owner.
     */
    @GetMapping(path = "/me")
    public ShopOwnerDetailDto getShopSettings(HttpServletRequest servletRequest) throws LoginException {
        Shop shop = authenticationHelper.authenticateShop(servletRequest);

        return ShopOwnerDetailDto.of(shop);
    }

    /**
     * Creates a link to the shop creation page. This link contains a token to authenticate the caller.
     *
     * @param request the create link request.
     */
    @PostMapping(path = "/send-create-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendCreateLink(@Valid @RequestBody SendCreateLinkDto request) {
        shopService.sendCreateLink(request.getEmail());
    }

    /**
     * Creates a passwort reset link. The link contains a token that authenticates to authenticate the caller.
     *
     * @param request the reset password request.
     */
    @PostMapping(path = "/send-password-reset-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendPasswordResetLink(@Valid @RequestBody SendPasswordResetLinkDto request) {
        shopLoginService.sendPasswordResetLink(request.getEmail());
    }

    /**
     * Resets the user password.
     *
     * @param request the password reset request.
     * @param token   the token to authenticate the reset request.
     * @throws LoginException             if the caller is not authenticated to perform a password request.
     * @throws ShopLoginNotFoundException if the caller's E-Mail address is not found in the database.
     */
    @PostMapping(path = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void resetPassword(@Valid @RequestBody ResetPasswordDto request, @RequestParam @NotBlank String token) throws LoginException, ShopLoginNotFoundException {
        String email = tokenService.verifyPasswordResetToken(PasswordResetToken.of(token));

        shopLoginService.resetPassword(email, request.getPassword());
    }

    /**
     * Creates a new shop.
     *
     * @param request the shop creation request.
     * @param token   the token to authenticate the caller.
     * @return the newly created shop as {@link ShopDetailDto}.
     * @throws LoginException             if the caller is not authenticated to create a new shop.
     * @throws ShopAlreadyExistsException if a shop with the given E-Mail already exists.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ShopDetailDto createShop(@Valid @RequestBody CreateShopDto request, @RequestParam @NotBlank String token) throws LoginException, ShopAlreadyExistsException {
        // The token is taken from the link which the user got with email
        // It contains the email address, and is used to verify that the user really has access to this email address
        String email = tokenService.verifyShopCreationToken(ShopCreationToken.of(token));

        return ShopDetailDto.of(
            shopService.create(new ShopCreation(
                email,
                request.getOwnerName(),
                request.getName(),
                request.getStreet(),
                request.getZipCode(),
                request.getCity(),
                request.getAddressSupplement(),
                request.getDetails(),
                request.getWebsite(),
                request.getPassword(),
                Lists.map(request.getContacts(), CreateContactDto::toContactCreation),
                request.getSlots().toSlots()
            )));
    }

    /**
     * Updates shop details.
     *
     * @param request        the update shop request.
     * @param servletRequest an instance of {@link HttpServletRequest}.
     * @return the updated shop as {@link ShopDetailDto}.
     * @throws LoginException if the caller is not authenticated to modify this shop.
     */
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ShopDetailDto updateShop(@Valid @RequestBody UpdateShopDto request, HttpServletRequest servletRequest) throws LoginException {
        Shop shop = authenticationHelper.authenticateShop(servletRequest);

        return ShopDetailDto.of(shopService.update(shop, new ShopUpdate(
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

    /**
     * Retrieves nearby shops given a zip code.
     *
     * @param zipCode the zip code as String.
     * @return a list of shops as {@link ShopListDto}.
     */
    @GetMapping("nearby")
    public ShopListDto listNearby(@RequestParam @NotBlank String zipCode) {
        return ShopListDto.of(shopService.findNearby(zipCode));
    }

    /**
     * Retrieves nearby shops given a zip code.
     *
     * @param query   a search string to match locations.
     * @param zipCode the zip code as String.
     * @return a list of shops as {@link ShopListDto}.
     */
    @GetMapping("search")
    public ShopListDto listNearby(@RequestParam @NotBlank String query, @NotBlank @RequestParam String zipCode) {
        return ShopListDto.of(shopService.search(query, zipCode));
    }
}
