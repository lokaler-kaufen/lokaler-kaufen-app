package de.qaware.mercury.mercury.business.shop;

import lombok.Getter;

public enum ContactType {

    WHATSAPP("WHATSAPP"),
    PHONE("PHONE"),
    FACETIME("FACETIME"),
    GOOGLE_DUO("GOOGLE_DUO"),
    SKYPE("SKYPE"),
    SIGNAL_PRIVATE_MESSENGER("SIGNAL_PRIVATE_MESSENGER"),
    VIBER("VIBER");

    @Getter
    private final String value;

    ContactType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
