package de.qaware.mercury.business.shop;

import lombok.Value;

import java.time.LocalTime;

@Value
public class DayConfig {
    LocalTime start;
    LocalTime end;
}
