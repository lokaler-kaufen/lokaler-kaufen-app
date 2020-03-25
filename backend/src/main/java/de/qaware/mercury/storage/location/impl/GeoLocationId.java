package de.qaware.mercury.storage.location.impl;

import lombok.Data;

import java.io.Serializable;

@Data
public class GeoLocationId implements Serializable {
    String countryCode;
    String zipCode;
    String placeName;
}