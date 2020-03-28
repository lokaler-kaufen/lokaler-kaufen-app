package de.qaware.mercury.business.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.lang.Nullable;

import java.util.List;

@Value
@AllArgsConstructor
@Builder
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
    String password;
    // Maps from WHATSAPP -> Telephone number, for example
    List<ContactCreation> contacts;
    SlotConfig slotConfig;
}
