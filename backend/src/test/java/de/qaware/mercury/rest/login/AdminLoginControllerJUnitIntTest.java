package de.qaware.mercury.rest.login;

import de.qaware.mercury.business.login.AdminLoginService;
import de.qaware.mercury.test.plumbing.CustomActiveProfileResolver;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(resolver = CustomActiveProfileResolver.class)
// Spring will rollback any changes in the test methods
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminLoginControllerJUnitIntTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    AdminLoginService adminLoginService;

    @Test
    void login_and_get_user_info() throws Exception {
        // Given an admin account
        adminLoginService.createLogin("admin-1@localhost", "admin-1");

        // When we login, we get a token in a cookie
        Cookie cookie = mvc.perform(post("/api/admin/login").contentType(MediaType.APPLICATION_JSON).content(
            loadJson("/request/AdminLoginControllerJUnitIntTest/LoginDto.json")
        ))
            .andExpect(status().isOk())
            .andExpect(cookie().httpOnly("mercury-admin", true))
            .andReturn().getResponse().getCookie("mercury-admin");

        // When we call the whoami endpoint with that cookie, then we get our email address
        mvc.perform(get("/api/admin/login").cookie(cookie))
            .andExpect(status().isOk())
            .andExpect(contentIsJson("/response/AdminLoginControllerJUnitIntTest/WhoAmIDto.json"));
    }

    private static ResultMatcher contentIsJson(String resource) throws IOException {
        return content().json(loadJson(resource), true);
    }

    private static String loadJson(String resource) throws IOException {
        try (InputStream stream = AdminLoginControllerJUnitIntTest.class.getResourceAsStream(resource)) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}