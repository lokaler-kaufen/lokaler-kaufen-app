package de.qaware.mercury.mercury.business.time.impl;

import de.qaware.mercury.mercury.business.time.Clock;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class WallClock implements Clock {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}
