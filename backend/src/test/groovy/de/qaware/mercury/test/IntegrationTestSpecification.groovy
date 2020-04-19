package de.qaware.mercury.test


import de.qaware.mercury.test.plumbing.CustomActiveProfileResolver
import de.qaware.mercury.test.plumbing.IntegrationTest
import groovy.transform.TypeChecked
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

/**
 * Abstract class for every integration test.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(resolver = CustomActiveProfileResolver.class)
// Spring will rollback any changes in the test methods
@Transactional
@TypeChecked
@IntegrationTest
abstract class IntegrationTestSpecification extends Specification {
    static final ObjectMapper mapper = new ObjectMapper();

    static <T> T contentAsObject(MockHttpServletResponse response, Class<T> clazz) {
        return mapper.readValue(response.getContentAsByteArray(), clazz);
    }

    static Map<String, Object> contentAsMap(MockHttpServletResponse response) {
        return mapper.readValue(response.getContentAsByteArray(), Map.class);
    }
}