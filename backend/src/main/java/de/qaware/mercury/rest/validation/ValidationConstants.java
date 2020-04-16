package de.qaware.mercury.rest.validation;

public final class ValidationConstants {
    public static final int MIN_PASSWORD_LENGTH = 12;
    public static final int MAX_PASSWORD_LENGTH = 70;

    /**
     * You have to enable {@link java.util.regex.Pattern#CASE_INSENSITIVE} when using this regex.
     */
    // From https://www.regular-expressions.info/email.html
    public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$";

    private ValidationConstants() {
    }
}
