package de.qaware.mercury.business.email.impl;

/**
 * Contains all placeholders for e-mail texts.
 */
public final class EmailTemplateConstants {
    private EmailTemplateConstants() {
        // hide constructor; no instances needed.
    }

    public static final String TOKEN = "{{ token }}";
    public static final String SHOP_NAME = "{{ shopName }}";
    public static final String CUSTOMER_NAME = "{{ customerName }}";
    public static final String OWNER_NAME = "{{ ownerName }}";
    public static final String CONTACT_TYPE = "{{ contactType }}";
    public static final String SLOT_DATE = "{{ date }}";
    public static final String SLOT_START_TIME = "{{ start }}";
    public static final String SLOT_END_TIME = "{{ end }}";
    public static final String CONTACT = "{{ contact }}";
    public static final String SHOP_CREATION_LINK = "{{ link }}";
    public static final String CANCEL_RESERVATION_LINK = "{{ cancelReservationLink }}";
    @SuppressWarnings("java:S2068") // Shut up SonarQube :/
    public static final String PASSWORD_RESET_LINK = "{{ link }}";

    public static final String SHOP_EMAIL_ADDRESS = "{{ email }}";
    public static final String SHOP_STREET = "{{ street }}";
    public static final String SHOP_ZIP_CODE = "{{ zipCode }}";
    public static final String SHOP_CITY = "{{ city }}";
    public static final String SHOP_ADDRESS_SUPPLEMENT = "{{ addressSupplement }}";
    public static final String SHOP_WEBSITE = "{{ website }}";
    public static final String SHOP_DETAILS = "{{ details }}";

    public static final String ADMIN_UI_LINK = "{{ adminUiLink }}";
}
