package de.qaware.mercury.rest.info


import de.qaware.mercury.test.IntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

class InfoRestControllerIntTest extends IntegrationTestSpecification {
    @Autowired
    MockMvc mockMvc

    def "version"() {
        when: "we fetch the version information"
        ResultActions result = mockMvc.perform(get("/api/info/version"))

        then: "we get something meaningful"
        MockHttpServletResponse response = result
            .andExpect(status().isOk())
            .andReturn().getResponse()

        Map<String, Object> versionDto = contentAsMap(response)
        versionDto['commitHash'] != null
        versionDto['version'] != null
        versionDto['commitTime'] != null
    }
}
