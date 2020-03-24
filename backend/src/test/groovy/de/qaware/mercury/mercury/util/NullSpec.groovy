package de.qaware.mercury.mercury.util

import spock.lang.Specification
import spock.lang.Unroll

class NullSpec extends Specification {
    @Unroll
    def "Check nullable mapping of #input to #result"() {

        expect:
        Null.map(input, mapper) == result

        where:
        input   | mapper                 | result
        null    | null                   | null
        "hello" | { it.size() }          | 5
        "hello" | { it.concat('world') } | 'helloworld'
    }
}
