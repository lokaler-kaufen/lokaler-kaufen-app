package de.qaware.mercury.util

import spock.lang.Specification

class ListsSpec extends Specification {
    def "Check mapping of List to List"() {
        given:
        List<String> input = ['a', 'ab', 'abc', 'ab', 'a'] as List

        when:
        List<Integer> output = Lists.map(input, { it.size() })

        then:
        output.size() == 5
        output[0] == 1
        output[1] == 2
        output[2] == 3
        output[3] == 2
        output[4] == 1
    }
}
