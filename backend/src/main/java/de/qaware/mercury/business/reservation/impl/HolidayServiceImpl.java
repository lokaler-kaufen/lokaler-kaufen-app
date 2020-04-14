package de.qaware.mercury.business.reservation.impl;

import de.jollyday.HolidayManager;
import de.jollyday.parameter.UrlManagerParameter;
import de.qaware.mercury.business.location.FederalState;
import de.qaware.mercury.business.reservation.HolidayService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Uses Jollyday to calculate holidays.
 */
@Service
class HolidayServiceImpl implements HolidayService {
    private final HolidayManager jollyday;

    public HolidayServiceImpl() {
        jollyday = HolidayManager.getInstance(new UrlManagerParameter(
            HolidayServiceImpl.class.getResource("/jollyday/german-holidays.xml"), null
        ));
    }

    @Override
    public boolean isHoliday(LocalDate date, FederalState federalState) {
        return jollyday.isHoliday(date, federalState.getCode());
    }
}
