package de.qaware.mercury.business.validation;

public final class Validation {
    private Validation() {
    }

    private static final String VALID_UUID = "^[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12}$";
    public static final String SHOP_ID = VALID_UUID;

    public static final int MIN_PASSWORD_LENGTH = 12;
    public static final int MAX_PASSWORD_LENGTH = 70;

    /**
     * You have to enable {@link java.util.regex.Pattern#CASE_INSENSITIVE} when using this regex.
     */
    // From https://www.regular-expressions.info/email.html
    public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$";
}
