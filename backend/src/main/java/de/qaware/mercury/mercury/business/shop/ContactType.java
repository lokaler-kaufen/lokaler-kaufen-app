package de.qaware.mercury.mercury.business.shop;

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
}
