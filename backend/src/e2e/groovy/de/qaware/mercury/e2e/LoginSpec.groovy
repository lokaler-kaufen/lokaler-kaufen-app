package de.qaware.mercury.e2e

import geb.spock.GebSpec
import spock.lang.Stepwise
import spock.lang.Unroll

@Stepwise
class LoginSpec extends GebSpec {

    def "Login button is disabled on fresh login page"() {
        given:
        to LoginPage

        expect:
        at LoginPage
        loginButton.isDisabled()
    }

    @Unroll
    def "Check disabled login button for #mail"() {
        given:
        password.text = pwd
        email.text = mail

        expect:
        loginButton.isDisabled()

        where:
        mail                  | pwd
        "test@lokaler.kaufen" | ""
        "test"                | "holy"
        "test@"               | "secret"
        "test@domain."        | ""
    }

    def "We can not login with unknown user"() {
        when:
        email.text = 'test@lokaler.kaufen'
        password.text = '1qay2wsx'
        login()

        then:
        loginButton.isEnabled()
        waitFor { error }.text().startsWith('Sorry')
    }
}
