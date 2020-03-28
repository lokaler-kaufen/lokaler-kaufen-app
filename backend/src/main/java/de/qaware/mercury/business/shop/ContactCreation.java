package de.qaware.mercury.business.shop;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ContactCreation {
    ContactType contactType;
    String data;
}
