package de.qaware.mercury.mercury.plumbing

import spock.lang.Specification

class SwaggerConfigSpec extends Specification {
    def "Check Swagger configuration bean"() {
        given:
        def factory = new SwaggerConfig()

        when:
        def api = factory.api()

        then:
        api
    }
}
