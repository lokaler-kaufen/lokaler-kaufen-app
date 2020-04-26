package de.qaware.mercury.rest.shop;

import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.PasswordResetToken;
import de.qaware.mercury.business.login.ShopCreationToken;
import de.qaware.mercury.business.login.ShopLogin;
import de.qaware.mercury.business.login.ShopLoginNotFoundException;
import de.qaware.mercury.business.login.ShopLoginService;
import de.qaware.mercury.business.login.ShopToken;
import de.qaware.mercury.business.login.TokenService;
import de.qaware.mercury.business.login.VerifiedToken;
import de.qaware.mercury.business.reservation.SlotService;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopAlreadyExistsException;
import de.qaware.mercury.business.shop.ShopCreation;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.business.validation.Validation;
import de.qaware.mercury.rest.shop.dto.request.CreateShopDto;
import de.qaware.mercury.rest.shop.dto.request.ResetPasswordDto;
import de.qaware.mercury.rest.shop.dto.request.SendCreateLinkDto;
import de.qaware.mercury.rest.shop.dto.request.SendPasswordResetLinkDto;
import de.qaware.mercury.rest.shop.dto.response.ShopDetailDto;
import de.qaware.mercury.rest.shop.dto.response.ShopListDto;
import de.qaware.mercury.rest.util.cookie.CookieHelper;
import de.qaware.mercury.util.Maps;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import static de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper.SHOP_COOKIE_NAME;

/**
 * This controller is used to retrieve shop DTOs for users and shop owners.
 */
@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/shop", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@SuppressWarnings("java:S4784")
    // JDK since 9 has additional protection against ReDos attacks
class ShopController {
    private final ShopService shopService;
    private final TokenService tokenService;
    private final ShopLoginService shopLoginService;
    private final CookieHelper cookieHelper;
    private final SlotService slotService;

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
            throw ShopNotFoundException.ofShopId(Shop.Id.parse(id));
        }

        return ShopDetailDto.of(shop, shopService);
    }

    /**
     * Creates a link to the shop creation page. This link contains a token to authenticate the caller.
     *
     * @param request the create link request.
     */
    @PostMapping(path = "/send-create-link", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void sendCreateLink(@Valid @RequestBody SendCreateLinkDto request) throws ShopAlreadyExistsException {
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
     * @param request  the shop creation request.
     * @param token    the token to authenticate the caller.
     * @param response http response
     * @return the newly created shop as {@link ShopDetailDto}.
     * @throws LoginException             if the caller is not authenticated to create a new shop.
     * @throws ShopAlreadyExistsException if a shop with the given E-Mail already exists.
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ShopDetailDto createShop(@Valid @RequestBody CreateShopDto request, @RequestParam @NotBlank String token, HttpServletResponse response) throws LoginException, ShopAlreadyExistsException, LocationNotFoundException {
        // The token is taken from the link which the user got with email
        // It contains the email address, and is used to verify that the user really has access to this email address
        String email = tokenService.verifyShopCreationToken(ShopCreationToken.of(token));

        SlotConfig slotConfig = request.getSlots().toSlots();
        Shop createdShop = shopService.create(new ShopCreation(
            email,
            request.getOwnerName(),
            request.getName(),
            request.getStreet(),
            request.getZipCode(),
            request.getCity(),
            request.getAddressSupplement(),
            request.getDetails(),
            request.getWebsite(),
            request.isAutoColorEnabled(),
            request.getPassword(),
            Maps.mapKeys(request.getContacts(), ContactType::parse),
            slotConfig,
            request.getSocialLinks().toSocialLinks(),
            request.getBreaks().toBreaks()
        ));

        // When the shop is created, the client is automatically logged in
        VerifiedToken<ShopLogin.Id, ShopToken> loginToken = shopLoginService.login(email, request.getPassword());
        cookieHelper.addTokenCookie(SHOP_COOKIE_NAME, loginToken, response);

        return ShopDetailDto.of(createdShop, shopService);
    }

    /**
     * Retrieves nearby shops, optionally within the given maxDistance of zipCode.
     *
     * @param zipCode     the zip code as String.
     * @param maxDistance the maximum distance from the given zipCode in km (optional, will return all shops if omitted)
     * @return a list of shops as {@link ShopListDto}.
     */
    @GetMapping("nearby")
    public ShopListDto findActive(@RequestParam @NotBlank String zipCode, @RequestParam(required = false) @Nullable Integer maxDistance) throws LocationNotFoundException {
        if (maxDistance != null) {
            return ShopListDto.of(shopService.findActive(zipCode, maxDistance), shopService);
        }
        return ShopListDto.of(shopService.findActive(zipCode), shopService);
    }

    /**
     * Retrieves active and enabled shops matching the given search query, optionally within the given maxDistance of zipCode
     *
     * @param query       a search string to match locations.
     * @param zipCode     the zip code as String.
     * @param maxDistance the maximum distance from the given zip code in km (optional, will return all matching shops if omitted)
     * @return a list of shops as {@link ShopListDto}.
     */
    @GetMapping("search")
    public ShopListDto searchActive(@RequestParam @NotBlank String query, @NotBlank @RequestParam String zipCode, @RequestParam(required = false) @Nullable Integer maxDistance) throws LocationNotFoundException {
        if (maxDistance != null) {
            return ShopListDto.of(shopService.searchActive(query, zipCode, maxDistance), shopService);
        }
        return ShopListDto.of(shopService.searchActive(query, zipCode), shopService);
    }
}
