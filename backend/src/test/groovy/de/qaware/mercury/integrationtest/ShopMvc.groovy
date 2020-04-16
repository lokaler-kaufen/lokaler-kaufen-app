package de.qaware.mercury.integrationtest

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

import javax.servlet.http.Cookie

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

final class ShopMvc {
    private ShopMvc() {}

    /**
     * Logs in a shop.
     *
     * @param mvc MockMvc
     * @param email email of the shop
     * @param password password of the shop
     * @return login token in a cookie
     */
    static Cookie loginShop(MockMvc mvc, String email, String password) {
        mvc
            .perform(post("/api/shop/login").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "email": "$email",
                  "password": "$password"
                }
                """
            ))
            .andExpect(status().isOk())
            .andExpect(cookie().httpOnly("mercury-shop", true))
            .andReturn().getResponse().getCookie("mercury-shop")
    }
}
