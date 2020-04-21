package de.qaware.mercury.rest.reservation

import de.qaware.mercury.business.shop.DayConfig
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.test.IntegrationTestSpecification
import de.qaware.mercury.test.builder.SlotConfigBuilder
import de.qaware.mercury.test.email.Email
import de.qaware.mercury.test.email.Emails
import de.qaware.mercury.test.email.TestEmailSender
import de.qaware.mercury.test.fixtures.ShopCreationFixtures
import de.qaware.mercury.test.time.TestClock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import java.time.LocalTime
import java.time.ZonedDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReservationControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc

    @Autowired
    ShopService shopService

    @Autowired
    TestEmailSender testEmailSender

    def "previewSlots"() {
        when: "we preview the slots"
        ResultActions result = mvc.perform(post("/api/reservation/preview").contentType(MediaType.APPLICATION_JSON).content(
            """
            {
              "timePerSlot": 30,
              "timeBetweenSlots": 30,
              "delayForFirstSlot": 0,
              "monday": {
                "start": "10:00",
                "end": "12:00"
              }
            }
            """
        ))

        then: "we get the slot preview"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                    "start": "2020-04-20",
                    "end": "2020-04-26",
                    "days": [
                        {
                            "date": "2020-04-20",
                            "dayOfWeek": "MONDAY",
                            "holiday": false,
                            "slots": [
                                {
                                    "id": "2020-04-20T10:00",
                                    "start": "10:00",
                                    "end": "10:30",
                                    "available": true
                                },
                                {
                                    "id": "2020-04-20T11:00",
                                    "start": "11:00",
                                    "end": "11:30",
                                    "available": true
                                }
                            ]
                        },
                        {
                            "date": "2020-04-21",
                            "dayOfWeek": "TUESDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-22",
                            "dayOfWeek": "WEDNESDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-23",
                            "dayOfWeek": "THURSDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-24",
                            "dayOfWeek": "FRIDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-25",
                            "dayOfWeek": "SATURDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-26",
                            "dayOfWeek": "SUNDAY",
                            "holiday": false,
                            "slots": []
                        }
                    ]
                }
                """
            ))
    }

    def "create a reservation and cancel it"() {
        given: "a shop"
        Shop shop = shopService.create(ShopCreationFixtures.create(
            new SlotConfigBuilder().setDelayForFirstSlot(0).setTimeBetweenSlots(30).setTimePerSlot(30)
                .setMonday(new DayConfig(LocalTime.of(10, 0), LocalTime.of(12, 0))).build()
        ))
        testEmailSender.getEmails().clear()

        when: "we list the slots"
        ResultActions result = mvc.perform(get("/api/reservation/{shopId}/slot", shop.id))

        then: "we get the available slots"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                    "start": "2020-04-16",
                    "end": "2020-04-22",
                    "days": [
                        {
                            "date": "2020-04-16",
                            "dayOfWeek": "THURSDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-17",
                            "dayOfWeek": "FRIDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-18",
                            "dayOfWeek": "SATURDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-19",
                            "dayOfWeek": "SUNDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-20",
                            "dayOfWeek": "MONDAY",
                            "holiday": false,
                            "slots": [
                                {
                                    "id": "2020-04-20T10:00",
                                    "start": "10:00",
                                    "end": "10:30",
                                    "available": true
                                },
                                {
                                    "id": "2020-04-20T11:00",
                                    "start": "11:00",
                                    "end": "11:30",
                                    "available": true
                                }
                            ]
                        },
                        {
                            "date": "2020-04-21",
                            "dayOfWeek": "TUESDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-22",
                            "dayOfWeek": "WEDNESDAY",
                            "holiday": false,
                            "slots": []
                        }
                    ]
                }
                """
            ))

        when: "we reserve a slot"
        result = mvc.perform(post("/api/reservation/{shopId}", shop.id).contentType(MediaType.APPLICATION_JSON).content(
            """
            {
              "slotId": "2020-04-20T10:00",
              "contactType": "WHATSAPP",
              "contact": "12345",
              "name": "Mr. Name",
              "email": "mr-name@local.host"
            }
            """
        ))

        then: "the request succeeded"
        result.andExpect(status().isOk())

        and: "the shop owner got an email"
        Emails shopEmails = testEmailSender.getEmails().findWithRecipient(shop.getEmail())

        shopEmails.size() == 1
        Email shopEmail = shopEmails.get(0)
        shopEmail.getBody().contains("WhatsApp")
        shopEmail.getBody().contains("am 20.04.2020 zwischen 10:00 Uhr und 10:30 Uhr")
        shopEmail.getBody().contains("12345")
        shopEmail.getBody().contains("Mr. Name")
        shopEmail.getBody().contains("?token")

        and: "the customer got an email"
        Emails customerEmails = testEmailSender.getEmails().findWithRecipient("mr-name@local.host")

        customerEmails.size() == 1
        Email customerEmail = customerEmails.get(0)
        customerEmail.getBody().contains(shop.name)
        customerEmail.getBody().contains("WhatsApp")
        customerEmail.getBody().contains("am 20.04.2020 zwischen 10:00 Uhr und 10:30 Uhr")
        customerEmail.getBody().contains("?token")
        String cancellationToken = customerEmail.findToken()

        when: "we list the slots"
        result = mvc.perform(get("/api/reservation/{shopId}/slot", shop.id))

        then: "the slot is marked as reserved"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                    "start": "2020-04-16",
                    "end": "2020-04-22",
                    "days": [
                        {
                            "date": "2020-04-16",
                            "dayOfWeek": "THURSDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-17",
                            "dayOfWeek": "FRIDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-18",
                            "dayOfWeek": "SATURDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-19",
                            "dayOfWeek": "SUNDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-20",
                            "dayOfWeek": "MONDAY",
                            "holiday": false,
                            "slots": [
                                {
                                    "id": "2020-04-20T10:00",
                                    "start": "10:00",
                                    "end": "10:30",
                                    "available": false
                                },
                                {
                                    "id": "2020-04-20T11:00",
                                    "start": "11:00",
                                    "end": "11:30",
                                    "available": true
                                }
                            ]
                        },
                        {
                            "date": "2020-04-21",
                            "dayOfWeek": "TUESDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-22",
                            "dayOfWeek": "WEDNESDAY",
                            "holiday": false,
                            "slots": []
                        }
                    ]
                }
                """
            ))

        when: "we cancel the reservation"
        testEmailSender.getEmails().clear()
        mvc.perform(delete("/api/reservation?token=$cancellationToken"))

        then: "the request succeeded"
        result.andExpect(status().isOk())

        and: "the shop owner got an email"
        Emails shopEmails2 = testEmailSender.getEmails().findWithRecipient(shop.getEmail())
        shopEmails2.size() == 1
        Email shopEmail2 = shopEmails2.get(0)
        shopEmail2.getBody().contains("Mr. Name")
        shopEmail2.getBody().contains("am 20.04.2020 um 10:00 Uhr")

        and: "the customer got an email"
        Emails customerEmails2 = testEmailSender.getEmails().findWithRecipient("mr-name@local.host")
        customerEmails2.size() == 1
        Email customerEmail2 = shopEmails2.get(0)
        customerEmail2.getBody().contains("am 20.04.2020 um 10:00 Uhr")

        when: "we list the slots"
        result = mvc.perform(get("/api/reservation/{shopId}/slot", shop.id))

        then: "the slot is marked as available again"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                    "start": "2020-04-16",
                    "end": "2020-04-22",
                    "days": [
                        {
                            "date": "2020-04-16",
                            "dayOfWeek": "THURSDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-17",
                            "dayOfWeek": "FRIDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-18",
                            "dayOfWeek": "SATURDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-19",
                            "dayOfWeek": "SUNDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-20",
                            "dayOfWeek": "MONDAY",
                            "holiday": false,
                            "slots": [
                                {
                                    "id": "2020-04-20T10:00",
                                    "start": "10:00",
                                    "end": "10:30",
                                    "available": true
                                },
                                {
                                    "id": "2020-04-20T11:00",
                                    "start": "11:00",
                                    "end": "11:30",
                                    "available": true
                                }
                            ]
                        },
                        {
                            "date": "2020-04-21",
                            "dayOfWeek": "TUESDAY",
                            "holiday": false,
                            "slots": []
                        },
                        {
                            "date": "2020-04-22",
                            "dayOfWeek": "WEDNESDAY",
                            "holiday": false,
                            "slots": []
                        }
                    ]
                }
                """
            ))
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        TestClock testClock() {
            return new TestClock(ZonedDateTime.parse("2020-04-16T13:22:33.016252+02:00[Europe/Berlin]"));
        }

        @Bean
        @Primary
        TestEmailSender testEmailSender() {
            return new TestEmailSender()
        }
    }
}
