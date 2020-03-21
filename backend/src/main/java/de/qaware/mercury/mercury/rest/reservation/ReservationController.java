package de.qaware.mercury.mercury.rest.reservation;

import de.qaware.mercury.mercury.rest.reservation.dto.ReservationDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createReservation(@PathVariable("id") String shopId, @RequestBody ReservationDto reservationDetails) {

    }

}
