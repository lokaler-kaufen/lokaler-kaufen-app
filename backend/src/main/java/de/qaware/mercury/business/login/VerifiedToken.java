package de.qaware.mercury.business.login;

import lombok.Value;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Value
public class VerifiedToken<I, T extends Token> {
    I id;
    T token;
    Instant expiry;

    public long expiryInSeconds(ZonedDateTime now) {
        return now.toInstant().until(expiry, ChronoUnit.SECONDS);
    }
}
