package de.qaware.mercury.job;

import de.qaware.mercury.business.reservation.ReservationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * This job cleans up (anonymizes private data) expired reservations.
 */
@Slf4j
@Component
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class JanitorJob {
    private final ReservationService reservationService;

    /**
     * Anonymizes all reservations that expired the day before.
     */
    @Scheduled(cron = "${mercury.cleanup-job.reservation.cron}")
    public void cleanReservations() {
        log.info("Anonymizing all reservations that expired the day before ...");
        int count = reservationService.anonymizeExpired();
        log.info("... done anonymizing {} entries.", count);
    }
}
