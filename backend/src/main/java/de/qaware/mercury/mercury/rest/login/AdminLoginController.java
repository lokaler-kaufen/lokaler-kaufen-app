package de.qaware.mercury.mercury.rest.login;

import de.qaware.mercury.mercury.business.login.AdminLoginService;
import de.qaware.mercury.mercury.business.login.AdminToken;
import de.qaware.mercury.mercury.business.login.LoginException;
import de.qaware.mercury.mercury.rest.login.dto.LoginDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/api/admin/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
class AdminLoginController {
    public static final String ADMIN_COOKIE_NAME = "mercury-admin";

    private final AdminLoginService adminLoginService;

    AdminLoginController(AdminLoginService adminLoginService) {
        this.adminLoginService = adminLoginService;
    }

    @PostMapping
    void login(@RequestBody LoginDto request, HttpServletResponse response) throws LoginException {
        AdminToken token = adminLoginService.login(request.getEmail(), request.getPassword());

        Cookie cookie = new Cookie(ADMIN_COOKIE_NAME, token.getToken());
        cookie.setHttpOnly(true);
        // TODO MKA: set secure = true

        response.addCookie(cookie);
    }
}
