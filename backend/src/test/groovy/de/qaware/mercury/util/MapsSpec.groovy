package de.qaware.mercury.util

import spock.lang.Specification

class MapsSpec extends Specification {
    def "Check key mapping of Input map to Output map"() {
        given:
        LinkedHashMap<String, String> input = [hello: 'world', test: 'spock']

        when:
        Map<Integer, String> output = Maps.mapKeys(input, { it.size() })

        then:
        output.size() == 2
        output.get(4) == 'spock'
        output.get(5) == 'world'
    }
}
