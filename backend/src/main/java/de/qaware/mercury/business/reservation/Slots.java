package de.qaware.mercury.business.reservation;

import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
public class Slots {
    int days;
    LocalDate begin;
    List<Slot> slots;
}
