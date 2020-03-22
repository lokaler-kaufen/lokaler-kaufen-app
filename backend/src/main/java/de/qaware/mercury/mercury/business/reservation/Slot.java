package de.qaware.mercury.mercury.business.reservation;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class Slot {
    LocalDateTime start;
    LocalDateTime end;
    boolean available;
}
