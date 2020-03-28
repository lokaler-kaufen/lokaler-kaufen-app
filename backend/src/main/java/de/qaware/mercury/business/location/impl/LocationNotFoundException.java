package de.qaware.mercury.business.location.impl;

import de.qaware.mercury.business.BusinessException;

public class LocationNotFoundException extends BusinessException {
    public LocationNotFoundException(String location) {
        super(String.format("Location '%s' not found", location));
    }
}
