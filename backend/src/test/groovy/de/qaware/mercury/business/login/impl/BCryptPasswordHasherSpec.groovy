package de.qaware.mercury.business.login.impl

import spock.lang.Specification
import spock.lang.Unroll

class BCryptPasswordHasherSpec extends Specification {

    @Unroll
    def "Check hash for #password"() {
        given:
        def hasher = new BCryptPasswordHasher()

        when:
        def hash = hasher.hash(password)

        then:
        hasher.verify(password, hash)
        !hasher.verify(invalid, hash)

        where:
        password | invalid
        'test'   | 'not-test'
        'admin'  | 'not-admin'
        'hello'  | 'world'
    }
}
