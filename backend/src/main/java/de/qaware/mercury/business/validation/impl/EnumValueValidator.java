package de.qaware.mercury.business.validation.impl;

import de.qaware.mercury.business.validation.EnumValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * Validates that the CharSequence contains a value of the Enum referenced in {@link EnumValue#enumClass()}.
 * <p>
 * null is considered an invalid value.
 */
public class EnumValueValidator implements ConstraintValidator<EnumValue, CharSequence> {
    private Set<Object> acceptedValues;

    @Override
    public void initialize(EnumValue constraintAnnotation) {
        Enum<?>[] enumValues = constraintAnnotation.enumClass().getEnumConstants();

        acceptedValues = new HashSet<>(enumValues.length);
        for (Enum<?> enumValue : enumValues) {
            acceptedValues.add(enumValue.name());
        }
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return acceptedValues.contains(value.toString());
    }
}
