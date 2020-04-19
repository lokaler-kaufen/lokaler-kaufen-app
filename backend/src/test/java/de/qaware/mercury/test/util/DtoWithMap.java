package de.qaware.mercury.test.util;

import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.validation.EnumValue;

import java.util.Map;

/**
 * A DTO used in the {@link de.qaware.mercury.business.validation.impl.EnumValueValidatorTest}.
 * <p>
 * Can't be written in Groovy, because Groovy doesn't support type use annotations yet.
 */
public class DtoWithMap {
    private Map<@EnumValue(enumClass = ContactType.class) String, String> contactTypes;

    public DtoWithMap(Map<@EnumValue(enumClass = ContactType.class) String, String> contactTypes) {
        this.contactTypes = contactTypes;
    }
}