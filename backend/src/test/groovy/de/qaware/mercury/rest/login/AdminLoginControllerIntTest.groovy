package de.qaware.mercury.rest.login

import de.qaware.mercury.business.login.AdminEmailSettings
import de.qaware.mercury.business.login.AdminLoginService
import de.qaware.mercury.test.IntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import javax.servlet.http.Cookie

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class AdminLoginControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc
    @Autowired
    AdminLoginService adminLoginService

    def "login and get user info"() {
        given: "an admin account"
        // An admin account
        adminLoginService.createLogin("admin-1@lokaler.kaufen", "admin-1", AdminEmailSettings.noEmails())

        when: "we call the login endpoint"
        ResultActions result = mvc.perform(post("/api/admin/login").contentType(MediaType.APPLICATION_JSON).content(
            """
            {
              "email": "admin-1@lokaler.kaufen",
              "password": "admin-1"
            }
            """
        ))

        then: "we get a cookie containing the token"
        Cookie token = result
            .andExpect(status().isOk())
            .andExpect(cookie().httpOnly("mercury-admin", true))
            .andReturn().getResponse().getCookie("mercury-admin")

        when: "we call the whoami endpoint"
        result = mvc.perform(get("/api/admin/login").cookie(token))

        then: "we get our email address"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                  "email": "admin-1@lokaler.kaufen"
                }
                """
            ))

        when: "we call tokeninfo"
        result = mvc.perform(get("/api/admin/login/token-info").cookie(token))

        then: "we get logged in"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                  "status": "LOGGED_IN"
                }
                """
            ))

        when: "we log out"
        result = mvc.perform(delete("/api/admin/login"))

        then: "the cookie is gone"
        result
            .andExpect(cookie().maxAge("mercury-admin", -1))

        when: "we call the whoami endpoint"
        result = mvc.perform(get("/api/admin/login"))

        then: "we get unauthorized"
        result
            .andExpect(status().isUnauthorized())

        when: "we call tokeninfo"
        result = mvc.perform(get("/api/admin/login/token-info"))

        then: "we get not logged in"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                  "status": "NOT_LOGGED_IN"
                }
                """
            ))
    }
}
