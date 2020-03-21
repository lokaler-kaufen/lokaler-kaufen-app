package de.qaware.mercury.mercury.rest.plumbing.authentication;

import de.qaware.mercury.mercury.business.admin.Admin;
import de.qaware.mercury.mercury.business.login.AdminLoginService;
import de.qaware.mercury.mercury.business.login.AdminToken;
import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.business.login.ShopLoginService;
import de.qaware.mercury.mercury.business.login.ShopToken;
import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
@Slf4j
public class AuthenticationHelper {
    public static final String ADMIN_COOKIE_NAME = "mercury-admin";
    public static final String SHOP_COOKIE_NAME = "mercury-shop";

    private final AdminLoginService adminLoginService;
    private final ShopLoginService shopLoginService;

    public AuthenticationHelper(AdminLoginService adminLoginService, ShopLoginService shopLoginService) {
        this.adminLoginService = adminLoginService;
        this.shopLoginService = shopLoginService;
    }

    public Admin authenticateAdmin(HttpServletRequest request) {
        Cookie cookie = findCookie(request, ADMIN_COOKIE_NAME);
        String authorizationHeader = findAuthorizationHeader(request);

        if (authorizationHeader != null) {
            log.debug("Token found in Authorization header");
            try {
                return adminLoginService.verify(AdminToken.of(authorizationHeader));
            } catch (LoginException e) {
                throw new InvalidCredentialsRestException(e.getMessage());
            }
        }

        if (cookie != null) {
            log.debug("Token found in cookie");
            try {
                return adminLoginService.verify(AdminToken.of(cookie.getValue()));
            } catch (LoginException e) {
                throw new InvalidCredentialsRestException(e.getMessage());
            }
        }

        // Neither cookie nor header set
        log.debug("Neither cookie nor Authorization header contain token");
        throw new NotAuthenticatedRestException("No cookie or authorization header found");
    }

    public Shop authenticateShop(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SHOP_COOKIE_NAME);
        String authorizationHeader = findAuthorizationHeader(request);

        if (authorizationHeader != null) {
            log.debug("Token found in Authorization header");
            try {
                return shopLoginService.verify(ShopToken.of(authorizationHeader));
            } catch (LoginException e) {
                throw new InvalidCredentialsRestException(e.getMessage());
            }
        }

        if (cookie != null) {
            log.debug("Token found in cookie");
            try {
                return shopLoginService.verify(ShopToken.of(cookie.getValue()));
            } catch (LoginException e) {
                throw new InvalidCredentialsRestException(e.getMessage());
            }
        }

        // Neither cookie nor header set
        log.debug("Neither cookie nor Authorization header contain token");
        throw new NotAuthenticatedRestException("No cookie or authorization header found");
    }

    @Nullable
    private String findAuthorizationHeader(HttpServletRequest request) {
        String value = request.getHeader("Authorization");
        if (value == null) {
            return null;
        }

        if (!value.startsWith("Bearer ")) {
            log.debug("Authorization header found, but doesn't start with 'Bearer '");
            return null;
        }

        return value.substring("Bearer ".length());
    }

    @Nullable
    private Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (Objects.equals(cookieName, cookie.getName())) {
                return cookie;
            }
        }

        return null;
    }
}
