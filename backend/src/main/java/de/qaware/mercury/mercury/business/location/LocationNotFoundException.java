package de.qaware.mercury.mercury.business.location;

import de.qaware.mercury.mercury.business.BusinessException;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class LocationNotFoundException extends BusinessException {
    String location;
}
