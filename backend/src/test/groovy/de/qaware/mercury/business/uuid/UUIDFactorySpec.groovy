package de.qaware.mercury.business.uuid

import de.qaware.mercury.business.uuid.impl.UUIDFactoryImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = UUIDFactoryImpl)
class UUIDFactorySpec extends Specification {

    @Autowired
    UUIDFactory uuidFactory

    def "Create UUI with factory"() {
        given:
        UUID uuid1 = uuidFactory.create()
        UUID uuid2 = uuidFactory.create()

        expect:
        uuid1 != uuid2
    }
}
