package de.qaware.mercury.business.validation.impl

import de.qaware.mercury.business.shop.ContactType
import de.qaware.mercury.business.validation.EnumValue
import de.qaware.mercury.test.util.DtoWithMap
import spock.lang.Specification

import javax.validation.Validation
import javax.validation.Validator

class EnumValueValidatorTest extends Specification {
    def "check"(Object dto, boolean valid) {
        given:
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator()

        expect:
        validator.validate(dto).isEmpty() == valid

        where:
        dto                                     | valid
        new Dto("WHATSAPP")                     | true
        new Dto(null)                           | false
        new Dto("foobar")                       | false
        new DtoWithMap(Map.of())                | true
        new DtoWithMap(Map.of("WHATSAPP", "1")) | true
        new DtoWithMap(Map.of("foobar", "1"))   | false
    }

    static class Dto {
        @EnumValue(enumClass = ContactType.class)
        private String contactType;

        Dto(String contactType) {
            this.contactType = contactType
        }
    }
}
