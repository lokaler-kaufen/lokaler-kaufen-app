package de.qaware.mercury.util

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

    @Unroll
    def "Check or(#input, #alternative) == #result"() {
        expect:
        Null.or(input, alternative) == result

        where:
        input   | alternative | result
        null    | "foo"       | "foo"
        "hello" | "foo"       | "hello"
    }
}
