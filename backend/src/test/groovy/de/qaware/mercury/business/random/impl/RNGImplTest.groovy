package de.qaware.mercury.business.random.impl

import spock.lang.Specification
import spock.lang.Subject

class RNGImplTest extends Specification {
    @Subject
    RNGImpl rng = new RNGImpl()

    def "nextBytes"() {
        expect:
        byte[] random1 = rng.nextBytes(32)
        random1.size() == 32
        byte[] random2 = rng.nextBytes(32)
        random2.size() == 32

        random1 != random2
    }

    def "nextBytesSecure"() {
        expect:
        byte[] random1 = rng.nextBytesSecure(32)
        random1.size() == 32
        byte[] random2 = rng.nextBytesSecure(32)
        random2.size() == 32

        random1 != random2
    }
}
