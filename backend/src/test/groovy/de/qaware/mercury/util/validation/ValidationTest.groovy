package de.qaware.mercury.util.validation

import spock.lang.Specification

import java.util.regex.Pattern

class ValidationTest extends Specification {
    private static final Pattern EMAIL_REGEX = Pattern.compile(Validation.EMAIL_REGEX, Pattern.CASE_INSENSITIVE)

    def "test email regex"(String email, boolean matches) {
        expect:
        EMAIL_REGEX.matcher(email).matches() == matches

        where:
        email         | matches
        "foo@bar.org" | true
        "foo@bar"     | false
    }
}
