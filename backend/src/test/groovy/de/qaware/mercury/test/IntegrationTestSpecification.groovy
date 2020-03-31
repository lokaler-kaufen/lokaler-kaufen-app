package de.qaware.mercury.test


import de.qaware.mercury.test.plumbing.CustomActiveProfileResolver
import de.qaware.mercury.test.plumbing.IntegrationTest
import groovy.transform.TypeChecked
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional
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
}
