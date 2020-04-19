package de.qaware.mercury.rest.shop

import de.qaware.mercury.business.login.AdminEmailSettings
import de.qaware.mercury.business.login.AdminLoginService
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.integrationtest.AdminMvc
import de.qaware.mercury.test.IntegrationTestSpecification
import de.qaware.mercury.test.fixtures.ShopCreationFixtures
import de.qaware.mercury.test.fixtures.UpdateShopDtoFixtures
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.lang.Unroll

import javax.servlet.http.Cookie

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ShopAdminControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc

    @Autowired
    AdminLoginService adminLoginService

    @Autowired
    ShopService shopService

    @Unroll
    def "check authorization #method #url"(HttpMethod method, String url, String body) {
        expect:
        mvc.perform(request(method, url).contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isUnauthorized())

        where:
        method            | url                                                                          | body
        HttpMethod.GET    | "/api/admin/shop/992f885c-ae41-4691-9e0f-eb30e85735dd"                       | ""
        HttpMethod.GET    | "/api/admin/shop"                                                            | ""
        HttpMethod.PUT    | "/api/admin/shop/992f885c-ae41-4691-9e0f-eb30e85735dd/approve?approved=true" | ""
        HttpMethod.PUT    | "/api/admin/shop/992f885c-ae41-4691-9e0f-eb30e85735dd"                       | mapper.writeValueAsString(UpdateShopDtoFixtures.create())
        HttpMethod.DELETE | "/api/admin/shop/992f885c-ae41-4691-9e0f-eb30e85735dd"                       | ""
    }

    def "listAll"() {
        given: "an admin"
        adminLoginService.createLogin("admin-1@local.host", "admin-1", AdminEmailSettings.noEmails())
        Cookie token = AdminMvc.loginAdmin(mvc, "admin-1@local.host", "admin-1")

        and: "a shop"
        shopService.create(ShopCreationFixtures.create())

        when: "we list all shops"
        ResultActions result = mvc.perform(get("/api/admin/shop").cookie(token))

        then: "we get the shops"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                    "shops": [
                        {
                            "name": "name",
                            "ownerName": "owner",
                            "email": "shop-1@local.host",
                            "street": "street",
                            "zipCode": "81549",
                            "city": "München",
                            "addressSupplement": "addressSupplement",
                            "contacts": {
                                "WHATSAPP": "whatsapp"
                            },
                            "enabled": true,
                            "approved": false,
                            "details": "details",
                            "imageUrl": null,
                            "website": "https://local.host",
                            "slots": {
                                "timePerSlot": 15,
                                "timeBetweenSlots": 5,
                                "delayForFirstSlot": 0,
                                "monday": {
                                    "start": "07:00",
                                    "end": "15:00"
                                },
                                "tuesday": {
                                    "start": "08:00",
                                    "end": "16:00"
                                },
                                "wednesday": {
                                    "start": "10:00",
                                    "end": "18:00"
                                },
                                "thursday": {
                                    "start": "11:00",
                                    "end": "19:00"
                                },
                                "friday": {
                                    "start": "12:00",
                                    "end": "20:00"
                                },
                                "saturday": {
                                    "start": "10:00",
                                    "end": "12:00"
                                },
                                "sunday": null
                            },
                            "socialLinks": {
                                "instagram": "instagram",
                                "facebook": "facebook",
                                "twitter": "twitter"
                            },
                            "shareLink": "http://localhost:4200/bei/name"
                        }
                    ]
                }
                """))
    }

    def "getShopSettings"() {
        given: "an admin"
        adminLoginService.createLogin("admin-1@local.host", "admin-1", AdminEmailSettings.noEmails())
        Cookie token = AdminMvc.loginAdmin(mvc, "admin-1@local.host", "admin-1")

        and: "a shop"
        Shop shop = shopService.create(ShopCreationFixtures.create())

        when: "we get the shop details"
        ResultActions result = mvc.perform(get("/api/admin/shop/{id}", shop.id).cookie(token))

        then: "we get some"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                    "name": "name",
                    "ownerName": "owner",
                    "email": "shop-1@local.host",
                    "street": "street",
                    "zipCode": "81549",
                    "city": "München",
                    "addressSupplement": "addressSupplement",
                    "contacts": {
                        "WHATSAPP": "whatsapp"
                    },
                    "enabled": true,
                    "approved": false,
                    "details": "details",
                    "imageUrl": null,
                    "website": "https://local.host",
                    "slots": {
                        "timePerSlot": 15,
                        "timeBetweenSlots": 5,
                        "delayForFirstSlot": 0,
                        "monday": {
                            "start": "07:00",
                            "end": "15:00"
                        },
                        "tuesday": {
                            "start": "08:00",
                            "end": "16:00"
                        },
                        "wednesday": {
                            "start": "10:00",
                            "end": "18:00"
                        },
                        "thursday": {
                            "start": "11:00",
                            "end": "19:00"
                        },
                        "friday": {
                            "start": "12:00",
                            "end": "20:00"
                        },
                        "saturday": {
                            "start": "10:00",
                            "end": "12:00"
                        },
                        "sunday": null
                    },
                    "socialLinks": {
                        "instagram": "instagram",
                        "facebook": "facebook",
                        "twitter": "twitter"
                    },
                    "shareLink": "http://localhost:4200/bei/name"
                }
                """
            ))
    }
}
