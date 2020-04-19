package de.qaware.mercury.integrationtest

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc

import javax.servlet.http.Cookie

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

final class AdminMvc {
    private AdminMvc() {}

    /**
     * Logs in an admin.
     *
     * @param mvc MockMvc
     * @param email email of the admin
     * @param password password of the admin
     * @return login token in a cookie
     */
    static Cookie loginAdmin(MockMvc mvc, String email, String password) {
        mvc
            .perform(post("/api/admin/login").contentType(MediaType.APPLICATION_JSON).content(
                """
                {
                  "email": "$email",
                  "password": "$password"
                }
                """
            ))
            .andExpect(status().isOk())
            .andExpect(cookie().httpOnly("mercury-admin", true))
            .andReturn().getResponse().getCookie("mercury-admin")
    }
}
