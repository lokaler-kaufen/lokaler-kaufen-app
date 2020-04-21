package de.qaware.mercury.integrationtest


import de.qaware.mercury.business.login.AdminEmailSettings
import de.qaware.mercury.business.login.AdminLoginService
import de.qaware.mercury.rest.shop.dto.response.ShopsAdminDto
import de.qaware.mercury.test.IntegrationTestSpecification
import de.qaware.mercury.test.email.Emails
import de.qaware.mercury.test.email.TestEmailSender
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper

import javax.servlet.http.Cookie

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * 1. Create admin account
 * 2. Request shop creation link
 * 3. Use token from shop creation link to create shop
 * 4. Approve shop from admin
 * 5. Check if shop is in the list
 * 6. Login as shop
 * 7. See shop details
 */
class CreateShopIntegrationTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc

    @Autowired
    TestEmailSender testEmailSender

    @Autowired
    AdminLoginService adminLoginService

    ObjectMapper objectMapper = new ObjectMapper()

    def "Create shop and log in"() {
        given: "an admin login"
        adminLoginService.createLogin("admin-1@local.host", "admin-1", new AdminEmailSettings(true))

        when: "request the creation link"
        ResultActions response = mvc.perform(post("/api/shop/send-create-link").contentType(MediaType.APPLICATION_JSON).content(
            """
            {
              "email": "shop-1@local.host"
            }
            """
        ))

        then: "we get OK"
        response.andExpect(status().isOk())

        and: "we get an email containing the token for the shop creation"
        Emails emails = testEmailSender.getEmails().findWithRecipient("shop-1@local.host")
        emails.size() == 1
        String token = emails.get(0).findToken()

        when: "we use this token to create the shop"
        response = mvc.perform(post("/api/shop?token=$token").contentType(MediaType.APPLICATION_JSON).content(
            """
            {
              "ownerName": "owner-1",
              "name": "shop-1",
              "street": "street-1",
              "zipCode": "81549",
              "city": "city-1",
              "addressSupplement": "address-supplement-1",
              "details": "details-1",
              "website": "http://website-1.host",
              "password": "very-long-password",
              "contacts": {
                "WHATSAPP": "whatsapp-1",
                "SIGNAL": "signal-1"
              },
              "slots": {
                "timePerSlot": 15,
                "timeBetweenSlots": 5,
                "delayForFirstSlot": 60,
                "monday": {
                  "start": "09:00",
                  "end": "17:00"
                }
              },
              "socialLinks": {
                "instagram": "insta.gram",
                "facebook": "face.book",
                "twitter": "@twitter"
              },
              "breaks": {
                "monday": [
                  {
                    "start": "12:00",
                    "end": "13:00"
                  }
                ],
                "tuesday": [],
                "wednesday": [],
                "thursday": [],
                "friday": [],
                "saturday": [],
                "sunday": []
              }
            }
            """
        ))

        then: "we get OK"
        response.andExpect(status().isOk())

        when: "an admin logs in"
        response = mvc.perform(post("/api/admin/login").contentType(MediaType.APPLICATION_JSON).content("""
          {
             "email": "admin-1@local.host",
             "password": "admin-1"
          }
        """))

        then: "we get an admin login cookie"
        Cookie adminCookie = response.andExpect(status().isOk())
            .andExpect(cookie().exists("mercury-admin"))
            .andReturn().getResponse()
            .getCookie("mercury-admin")

        when: "we list the shops as admin"
        ShopsAdminDto shops = objectMapper.readValue(
            mvc.perform(get("/api/admin/shop").cookie(adminCookie))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsByteArray(), ShopsAdminDto.class
        )

        then: "then we find our shop"
        shops.getShops().size() == 1
        shops.getShops().get(0).getEmail() == "shop-1@local.host"

        when: "we we approve the shop"
        String shopId = shops.getShops().get(0).getId()
        response = mvc.perform(
            put("/api/admin/shop/$shopId/approve?approved=true").cookie(adminCookie)
        )

        then: "we get an ok"
        response.andExpect(status().isOk())

        when: "we list the shops"
        response = mvc.perform(get("/api/shop/nearby?zipCode=81549"))

        then: "we find our shop"
        response
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                  "shops": [
                    {
                      "name": "shop-1",
                      "supportedContactTypes": [
                        "WHATSAPP", "SIGNAL"
                      ]                
                    }
                  ]            
                }
                """
            ))

        when: "we log in as shop"
        response = mvc.perform(post("/api/shop/login").contentType(MediaType.APPLICATION_JSON).content(
            """
            {
              "email": "shop-1@local.host",
              "password": "very-long-password"
            }
            """
        ))

        then: "we get a shop login cookie"
        Cookie shopCookie = response.andExpect(status().isOk())
            .andExpect(cookie().exists("mercury-shop"))
            .andReturn().getResponse()
            .getCookie("mercury-shop")

        when: "we get the shop details"
        response = mvc.perform(get("/api/shop/me").cookie(shopCookie))

        then: "we get an ok and our shop details"
        response
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                    "name": "shop-1",
                    "ownerName": "owner-1",
                    "street": "street-1",
                    "zipCode": "81549",
                    "city": "city-1",
                    "addressSupplement": "address-supplement-1",
                    "details": "details-1",
                    "imageUrl": null,
                    "website": "http://website-1.host",
                    "contacts": {
                        "SIGNAL": "signal-1",
                        "WHATSAPP": "whatsapp-1"
                    },
                    "slots": {
                        "timePerSlot": 15,
                        "timeBetweenSlots": 5,
                        "delayForFirstSlot": 60,
                        "monday": {
                          "start": "09:00",
                          "end": "17:00"
                        },
                        "tuesday": null,
                        "wednesday": null,
                        "thursday": null,
                        "friday": null,
                        "saturday": null,
                        "sunday": null
                    },
                    "socialLinks": {
                        "instagram": "insta.gram",
                        "facebook": "face.book",
                        "twitter": "@twitter"
                    },
                    "breaks": {
                        "monday": [
                            {
                                "start": "12:00",
                                "end": "13:00"
                            }
                        ],
                        "tuesday": [],
                        "wednesday": [],
                        "thursday": [],
                        "friday": [],
                        "saturday": [],
                        "sunday": []
                    },
                    "shareLink": "http://localhost:4200/bei/shop1"
                }
                """
            ))
    }

    @TestConfiguration
    static class Configuration {
        @Bean
        @Primary
        TestEmailSender testEmailSender() {
            return new TestEmailSender()
        }
    }
}
