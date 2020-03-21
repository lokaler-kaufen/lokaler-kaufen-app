package de.qaware.mercury.mercury.business.shop;

import lombok.Value;

import java.time.LocalTime;

@Value
public class Slot {
    LocalTime start;
    LocalTime end;
}
