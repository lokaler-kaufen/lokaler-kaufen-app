package de.qaware.mercury.business.shop;

import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.Map;

@Value
public class ShopCreation {
    String email;
    String ownerName;
    String name;
    String street;
    String zipCode;
    String city;
    String addressSupplement;
    String details;
    @Nullable
    String website;
    boolean autoColorEnabled;
    String password;
    // Maps from WHATSAPP -> Telephone number, for example
    Map<ContactType, String> contacts;
    SlotConfig slotConfig;
    SocialLinks socialLinks;
    Breaks breaks;
}
