package de.qaware.mercury.mercury.util.validation;

public final class GuidValidation {

    private GuidValidation() {
    }

    public static final String REGEX = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";
}
