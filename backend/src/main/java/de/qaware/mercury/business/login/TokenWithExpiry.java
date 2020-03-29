package de.qaware.mercury.business.login;

import lombok.Value;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Value
public class TokenWithExpiry<T extends Token> {
    T token;
    ZonedDateTime expiry;

    public long expiryInSeconds(ZonedDateTime now) {
        return now.until(expiry, ChronoUnit.SECONDS);
    }
}
