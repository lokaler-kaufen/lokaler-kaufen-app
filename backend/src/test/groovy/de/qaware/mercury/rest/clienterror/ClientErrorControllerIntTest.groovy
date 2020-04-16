package de.qaware.mercury.rest.clienterror

import de.qaware.mercury.business.clienterror.ClientErrorService
import de.qaware.mercury.test.IntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import spock.mock.DetachedMockFactory

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class ClientErrorControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mvc

    @Autowired
    ClientErrorService clientErrorService

    def "reportError"() {
        when: "we report an error"
        ResultActions result = mvc.perform(post("/api/client-error").contentType(MediaType.APPLICATION_JSON).content(
            """
            {
              "traceId": "12345",
              "requestedUrl": "/local/host",
              "httpCode": 500,
              "body": "Oh noes an error occured!"
            }
            """
        ))

        then: "we get 200 OK"
        result.andExpect(status().isOk())

        and: "the error got reported"
        1 * clientErrorService.report({ reportedError ->
            reportedError.traceId == "12345"
            reportedError.requestedUrl == "/local/host"
            reportedError.httpCode == 500
            reportedError.body == "Oh noes an error occured!"
        })
    }

    @TestConfiguration
    static class MockConfig {
        DetachedMockFactory detachedMockFactory = new DetachedMockFactory()

        @Bean
        @Primary
        ClientErrorService clientErrorService() {
            return detachedMockFactory.Mock(ClientErrorService)
        }
    }
}
