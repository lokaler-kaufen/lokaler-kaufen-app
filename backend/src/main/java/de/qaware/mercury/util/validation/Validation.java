package de.qaware.mercury.util.validation;

public final class Validation {

    private Validation() {
    }

    private static final String VALID_UUID = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";
    public static final String SHOP_ID = VALID_UUID;
}
