package de.qaware.mercury.business.shop;

import lombok.Getter;

public enum ContactType {
    WHATSAPP("WHATSAPP", "WhatsApp"),
    PHONE("PHONE", "Telefon"),
    FACETIME("FACETIME", "FaceTime"),
    GOOGLE_DUO("GOOGLE_DUO", "Google Duo"),
    SKYPE("SKYPE", "Skype"),
    SIGNAL("SIGNAL", "Signal"),
    VIBER("VIBER", "Viber");

    @Getter
    private final String id;

    @Getter
    private final String humanReadable;

    ContactType(String id, String humanReadable) {
        this.id = id;
        this.humanReadable = humanReadable;
    }

    public static ContactType parse(String input) {
        for (ContactType contactType : ContactType.values()) {
            if (contactType.getId().equals(input)) {
                return contactType;
            }
        }

        throw new InvalidContactTypeException(input);
    }
}
