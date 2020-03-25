package de.qaware.mercury.business.shop;

import lombok.Getter;

public enum ContactType {
    WHATSAPP("WhatsApp"),
    PHONE("Telefon"),
    FACETIME("FaceTime"),
    GOOGLE_DUO("Google Duo"),
    SKYPE("Skype"),
    SIGNAL("Signal"),
    VIBER("Viber");

    @Getter
    private final String humanReadable;

    ContactType(String humanReadable) {
        this.humanReadable = humanReadable;
    }

    public static ContactType parse(String input) {
        try {
            return valueOf(input);
        } catch (IllegalArgumentException e) {
            throw new InvalidContactTypeException(input, e);
        }
    }
}
