package de.qaware.mercury.mercury.plumbing

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import springfox.documentation.spring.web.plugins.Docket

@ContextConfiguration(classes = SwaggerConfig)
class SwaggerConfigSpec extends Specification {

    @Autowired
    Docket api

    def "Check Swagger configuration bean"() {
        expect:
        api
    }
}
