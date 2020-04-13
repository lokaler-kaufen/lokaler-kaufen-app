package de.qaware.mercury.business.uuid.impl

import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.business.uuid.impl.UUIDFactoryImpl
import spock.lang.Specification
import spock.lang.Subject

class UUIDFactoryImplTest extends Specification {

    @Subject
    UUIDFactory uuidFactory = new UUIDFactoryImpl()

    def "Create UUI with factory"() {
        given:
        UUID uuid1 = uuidFactory.create()
        UUID uuid2 = uuidFactory.create()

        expect:
        uuid1 != uuid2
    }
}
