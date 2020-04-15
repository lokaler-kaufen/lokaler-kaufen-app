package de.qaware.mercury.rest.location

import de.qaware.mercury.test.IntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class LocationControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc

    def "getSuggestions"() {
        when: "we request the suggestions"
        ResultActions result = mvc.perform(get("/api/location/suggestion?zipCode=8154"))

        then: "we get some suggestions"
        result
            .andExpect(status().isOk())
            .andExpect(content().json(
                """
                {
                    "suggestions": [
                        {
                            "countryCode": "DE",
                            "zipCode": "81541",
                            "placeName": "München"
                        },
                        {
                            "countryCode": "DE",
                            "zipCode": "81543",
                            "placeName": "München"
                        },
                        {
                            "countryCode": "DE",
                            "zipCode": "81545",
                            "placeName": "München"
                        },
                        {
                            "countryCode": "DE",
                            "zipCode": "81547",
                            "placeName": "München"
                        },
                        {
                            "countryCode": "DE",
                            "zipCode": "81549",
                            "placeName": "München"
                        }
                    ]
                }
                """
            ))
    }

    def "isLocationKnown"() {
        when: "we check for an existing location"
        ResultActions result = mvc.perform(get("/api/location?zipCode=81549"))

        then: "we get a 200 OK"
        result.andExpect(status().isOk())

        when: "we check for a non-existing location"
        result = mvc.perform(get("/api/location?zipCode=12345"))

        then: "we get a 404 Not Found"
        result.andExpect(status().isNotFound())
    }
}
