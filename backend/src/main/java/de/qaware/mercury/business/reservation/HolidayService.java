package de.qaware.mercury.business.reservation;

import java.time.LocalDate;

public interface HolidayService {
    boolean isHoliday(LocalDate date, State state);
}
