package de.qaware.mercury.rest.plumbing.authentication;

import de.qaware.mercury.business.admin.Admin;
import de.qaware.mercury.business.login.AdminLoginService;
import de.qaware.mercury.business.login.AdminToken;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.ShopLogin;
import de.qaware.mercury.business.login.ShopLoginService;
import de.qaware.mercury.business.login.ShopToken;
import de.qaware.mercury.business.login.VerifiedToken;
import de.qaware.mercury.business.shop.Shop;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class AuthenticationHelper {
    public static final String ADMIN_COOKIE_NAME = "mercury-admin";
    public static final String SHOP_COOKIE_NAME = "mercury-shop";

    private final AdminLoginService adminLoginService;
    private final ShopLoginService shopLoginService;

    public Admin authenticateAdmin(HttpServletRequest request) throws LoginException {
        Cookie cookie = findCookie(request, ADMIN_COOKIE_NAME);
        String authorizationHeader = findAuthorizationHeader(request);

        if (authorizationHeader != null) {
            log.debug("Token found in Authorization header");
            return adminLoginService.verify(AdminToken.of(authorizationHeader));
        }

        if (cookie != null) {
            log.debug("Token found in cookie");
            return adminLoginService.verify(AdminToken.of(cookie.getValue()));
        }

        // Neither cookie nor header set
        log.debug("Neither cookie nor Authorization header contain token");
        throw LoginException.noCredentialsFound();
    }

    public Shop authenticateShop(HttpServletRequest request) throws LoginException {
        Cookie cookie = findCookie(request, SHOP_COOKIE_NAME);
        String authorizationHeader = findAuthorizationHeader(request);

        if (authorizationHeader != null) {
            log.debug("Token found in Authorization header");
            return shopLoginService.verify(ShopToken.of(authorizationHeader));
        }

        if (cookie != null) {
            log.debug("Token found in cookie");
            return shopLoginService.verify(ShopToken.of(cookie.getValue()));
        }

        // Neither cookie nor header set
        log.debug("Neither cookie nor Authorization header contain token");
        throw LoginException.noCredentialsFound();
    }

    @Nullable
    public VerifiedToken<Admin.Id, AdminToken> getAdminTokenInfo(HttpServletRequest request) {
        Cookie cookie = findCookie(request, ADMIN_COOKIE_NAME);
        String authorizationHeader = findAuthorizationHeader(request);

        if (authorizationHeader != null) {
            log.debug("Token found in Authorization header");
            return adminLoginService.getTokenInfo(AdminToken.of(authorizationHeader));
        }

        if (cookie != null) {
            log.debug("Token found in cookie");
            return adminLoginService.getTokenInfo(AdminToken.of(cookie.getValue()));
        }

        return null;
    }

    @Nullable
    public VerifiedToken<ShopLogin.Id, ShopToken> getShopTokenInfo(HttpServletRequest request) {
        Cookie cookie = findCookie(request, SHOP_COOKIE_NAME);
        String authorizationHeader = findAuthorizationHeader(request);

        if (authorizationHeader != null) {
            log.debug("Token found in Authorization header");
            return shopLoginService.getTokenInfo(ShopToken.of(authorizationHeader));
        }

        if (cookie != null) {
            log.debug("Token found in cookie");
            return shopLoginService.getTokenInfo(ShopToken.of(cookie.getValue()));
        }

        return null;
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
