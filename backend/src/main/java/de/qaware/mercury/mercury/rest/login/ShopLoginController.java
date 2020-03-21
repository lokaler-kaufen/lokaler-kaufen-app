package de.qaware.mercury.mercury.rest.login;

import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.login.ShopLoginService;
import de.qaware.mercury.mercury.business.login.ShopToken;
import de.qaware.mercury.mercury.rest.login.dto.LoginDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api/shop/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
class ShopLoginController {
    public static final String SHOP_COOKIE_NAME = "mercury-shop";

    private final ShopLoginService shopLoginService;

    ShopLoginController(ShopLoginService shopLoginService) {
        this.shopLoginService = shopLoginService;
    }

    @PostMapping
    void login(@RequestBody LoginDto request, HttpServletResponse response) throws LoginException {
        ShopToken token = shopLoginService.login(request.getEmail(), request.getPassword());

        Cookie cookie = new Cookie(SHOP_COOKIE_NAME, token.getToken());
        cookie.setHttpOnly(true);
        // TODO MKA: set secure = true

        response.addCookie(cookie);
    }
}
