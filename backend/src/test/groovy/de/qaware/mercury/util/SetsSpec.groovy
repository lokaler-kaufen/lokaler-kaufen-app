package de.qaware.mercury.util

import spock.lang.Specification

class SetsSpec extends Specification {
    def "Check mapping of List to Set"() {
        given:
        def input = ['a', 'ab', 'abc', 'ab', 'a'] as List

        when:
        def lengths = Sets.map(input, { it.size() })

        then:
        lengths.size() == 3
        lengths.contains(1)
        lengths.contains(2)
        lengths.contains(3)
    }
}
