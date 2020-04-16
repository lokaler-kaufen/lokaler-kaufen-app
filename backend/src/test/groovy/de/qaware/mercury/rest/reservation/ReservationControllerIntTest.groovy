package de.qaware.mercury.rest.reservation


import de.qaware.mercury.test.IntegrationTestSpecification
import de.qaware.mercury.test.time.FixedClock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import java.time.ZonedDateTime

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ReservationControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc

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
                    "slots": {
                        "0": [
                            {
                                "id": "2020-04-20T10:00",
                                "date": "2020-04-20",
                                "start": "10:00",
                                "end": "10:30",
                                "available": true
                            },
                            {
                                "id": "2020-04-20T11:00",
                                "date": "2020-04-20",
                                "start": "11:00",
                                "end": "11:30",
                                "available": true
                            }
                        ],
                        "1": [],
                        "2": [],
                        "3": [],
                        "4": [],
                        "5": [],
                        "6": []
                    }
                }
                """
            ))
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        @Primary
        FixedClock fixedClock() {
            return new FixedClock(ZonedDateTime.parse("2020-04-16T13:22:33.016252+02:00[Europe/Berlin]"));
        }
    }
}
