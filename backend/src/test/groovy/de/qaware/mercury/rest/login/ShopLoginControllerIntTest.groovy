package de.qaware.mercury.rest.login

import de.qaware.mercury.business.login.ShopLoginService
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.test.IntegrationTestSpecification
import de.qaware.mercury.test.fixtures.ShopCreationFixtures
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import javax.servlet.http.Cookie

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ShopLoginControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc
    @Autowired
    ShopService shopService
    @Autowired
    ShopLoginService shopLoginService

    def "login and get user info"() {
        given: "a shop and a shop login"
        shopService.create(ShopCreationFixtures.create())

        when: "we call the login endpoint"
        ResultActions result = mvc.perform(post("/api/shop/login").contentType(MediaType.APPLICATION_JSON).content(
            """
            {
              "email": "shop-1@local.host",
              "password": "shop-1"
            }
            """
        ))

        then: "we get a cookie containing the token"
        Cookie cookie = result
            .andExpect(status().isOk())
            .andExpect(cookie().httpOnly("mercury-shop", true))
            .andReturn().getResponse().getCookie("mercury-shop")

        when: "we call the whoami endpoint"
        result = mvc.perform(get("/api/shop/login").cookie(cookie))

        then: "we get our email address"
        ResultActions _ = result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                  "email": "shop-1@local.host"
                }
                """
            ))

    }
}
