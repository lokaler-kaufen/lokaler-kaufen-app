package de.qaware.mercury.rest.login;

import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.ShopLogin;
import de.qaware.mercury.business.login.ShopLoginService;
import de.qaware.mercury.business.login.ShopToken;
import de.qaware.mercury.business.login.VerifiedToken;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.time.Clock;
import de.qaware.mercury.rest.login.dto.request.LoginDto;
import de.qaware.mercury.rest.login.dto.response.TokenInfoDto;
import de.qaware.mercury.rest.login.dto.response.WhoAmIDto;
import de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper;
import de.qaware.mercury.rest.util.cookie.CookieHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static de.qaware.mercury.rest.plumbing.authentication.AuthenticationHelper.SHOP_COOKIE_NAME;

@RestController
@RequestMapping(path = "/api/shop/login", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class ShopLoginController {
    private final ShopLoginService shopLoginService;
    private final AuthenticationHelper authenticationHelper;
    private final CookieHelper cookieHelper;
    private final Clock clock;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public void login(@Valid @RequestBody LoginDto request, HttpServletResponse response) throws LoginException {
        VerifiedToken<ShopLogin.Id, ShopToken> token = shopLoginService.login(request.getEmail(), request.getPassword());

        cookieHelper.addTokenCookie(SHOP_COOKIE_NAME, token, response);
    }

    @DeleteMapping
    public void logout(HttpServletResponse response) {
        cookieHelper.removeCookie(SHOP_COOKIE_NAME, response);
    }

    @GetMapping
    public WhoAmIDto whoami(HttpServletRequest request) throws LoginException {
        Shop shop = authenticationHelper.authenticateShop(request);
        return new WhoAmIDto(shop.getEmail());
    }

    @GetMapping("/token-info")
    public TokenInfoDto tokenInfo(HttpServletRequest request) {
        VerifiedToken<ShopLogin.Id, ShopToken> token = authenticationHelper.getShopTokenInfo(request);
        if (token == null) {
            return TokenInfoDto.notLoggedIn();
        } else {
            return TokenInfoDto.loggedIn(token, clock.nowZoned());
        }
    }
}
