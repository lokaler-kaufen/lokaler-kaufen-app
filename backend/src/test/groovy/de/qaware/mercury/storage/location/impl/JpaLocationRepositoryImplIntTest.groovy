package de.qaware.mercury.storage.location.impl

import de.qaware.mercury.business.location.FederalState
import de.qaware.mercury.test.IntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired

import javax.persistence.EntityManager

class JpaLocationRepositoryImplIntTest extends IntegrationTestSpecification {
    @Autowired
    EntityManager entityManager

    def "test all federal state names"() {
        when: "we fetch all federal state names"
        List<String> allFederalStates = entityManager.createQuery("select distinct g.stateShort from GeoLocationEntity g", String.class).getResultList()

        then: "we get all 16 german federal states"
        allFederalStates.size() == 16

        and: "we can parse all of them into the FederalState enum"
        allFederalStates.each { FederalState.parse(it) }
    }
}
