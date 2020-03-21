package de.qaware.mercury.mercury.rest.login;

import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.login.ShopLoginService;
import de.qaware.mercury.mercury.business.login.ShopToken;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.rest.AuthenticationHelper;
import de.qaware.mercury.mercury.rest.login.dto.LoginDto;
import de.qaware.mercury.mercury.rest.login.dto.WhoAmIDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api/shop/login", produces = MediaType.APPLICATION_JSON_VALUE)
class ShopLoginController {
    private final ShopLoginService shopLoginService;
    private final AuthenticationHelper authenticationHelper;

    ShopLoginController(ShopLoginService shopLoginService, AuthenticationHelper authenticationHelper) {
        this.shopLoginService = shopLoginService;
        this.authenticationHelper = authenticationHelper;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void login(@RequestBody LoginDto request, HttpServletResponse response) throws LoginException {
        ShopToken token = shopLoginService.login(request.getEmail(), request.getPassword());

        Cookie cookie = new Cookie(AuthenticationHelper.SHOP_COOKIE_NAME, token.getToken());
        cookie.setHttpOnly(true);
        // TODO MKA: set secure = true

        response.addCookie(cookie);
    }

    @GetMapping
    WhoAmIDto whoami(HttpServletRequest request) {
        Shop shop = authenticationHelper.authenticateShop(request);
        return new WhoAmIDto(shop.getName());
    }
}
