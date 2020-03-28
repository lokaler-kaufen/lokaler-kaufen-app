package de.qaware.mercury.business.shop;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.List;

@Value
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor
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
    List<Contact> contacts;
    SlotConfig slotConfig;
}
