package de.qaware.mercury.business.reservation;

import de.qaware.mercury.business.location.FederalState;

import java.time.LocalDate;

public interface HolidayService {
    boolean isHoliday(LocalDate date, FederalState federalState);
}
