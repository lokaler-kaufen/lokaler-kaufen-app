package de.qaware.mercury.mercury.business.shop;

public enum ContactType {

    FACEBOOK_MESSENGER("FACEBOOK_MESSENGER"),
    GLIDE("GLIDE"),
    GOOGLE_DUO("GOOGLE_DUO"),
    WHATSAPP("WHATSAPP"),
    SKYPE("SKYPE"),
    JUSTALK("JUSTALK"),
    SIGNAL_PRIVATE_MESSENGER("SIGNAL_PRIVATE_MESSENGER"),
    SNAPCHAT("SNAPCHAT"),
    TANGO("TANGO"),
    VIBER("VIBER"),
    TELEPHONE("TELEPHONE");

    private final String value;

    ContactType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
