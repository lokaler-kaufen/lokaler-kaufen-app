package de.qaware.mercury.business.admin

import de.qaware.mercury.mercury.business.uuid.UUIDFactory
import spock.lang.Specification

class AdminSpec extends Specification {

    def "Check random Admin.Id"() {
        given:
        def uuid = UUID.randomUUID()
        def uuidFactory = Stub(UUIDFactory)
        uuidFactory.create() >> uuid

        when:
        def random = Admin.Id.random(uuidFactory)

        then:
        random
        random.id == uuid
        random.id.toString() == uuid.toString()
    }


    def "Check parse Admin.Id"() {
        given:
        def uuid = UUID.randomUUID()

        when:
        def parsed = Admin.Id.parse(uuid.toString())

        then:
        parsed
        parsed.id == uuid
        parsed.id.toString() == uuid.toString()
    }
}
