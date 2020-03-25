package de.qaware.mercury.business.shop;

import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.Map;

@Value
public class ShopUpdate {
    String name;
    String ownerName;
    String street;
    String zipCode;
    String city;
    String addressSupplement;
    String details;
    @Nullable
    String website;
    // Maps from WHATSAPP -> Telephone number, for example
    Map<ContactType, String> contactTypes;
    SlotConfig slotConfig;
}
