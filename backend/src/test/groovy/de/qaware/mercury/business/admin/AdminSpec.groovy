package de.qaware.mercury.business.admin

import de.qaware.mercury.business.uuid.UUIDFactory
import spock.lang.Specification

class AdminSpec extends Specification {

    def "Check random Admin.Id"() {
        given:
        UUID uuid = UUID.randomUUID()
        UUIDFactory uuidFactory = Stub(UUIDFactory)
        uuidFactory.create() >> uuid

        when:
        Admin.Id random = Admin.Id.random(uuidFactory)

        then:
        random
        random.id == uuid
        random.id.toString() == uuid.toString()
    }


    def "Check parse Admin.Id"() {
        given:
        UUID uuid = UUID.randomUUID()

        when:
        Admin.Id parsed = Admin.Id.parse(uuid.toString())

        then:
        parsed
        parsed.id == uuid
        parsed.id.toString() == uuid.toString()
    }
}
