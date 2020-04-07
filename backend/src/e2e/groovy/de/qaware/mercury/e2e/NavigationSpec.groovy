package de.qaware.mercury.e2e

import geb.spock.GebSpec
import spock.lang.Stepwise

@Stepwise
class NavigationSpec extends GebSpec {

    def "Can open Mercury start page"() {
        when:
        to StartPage

        then:
        at StartPage
        searchButton.isDisabled()
    }

    def "We can navigate to Imprint page"() {
        when:
        footer.imprint()

        then:
        at ImprintPage
        header.text() == 'Impressum'
    }

    def "We can navigate to Privacy page"() {
        when:
        footer.privacy()

        then:
        at PrivacyPage
        header.text() == 'Privacy'
    }

    def "We can navigate to Login page"() {
        when:
        footer.login()

        then:
        at LoginPage
        header.text().startsWith('Login')
    }
}
