package de.qaware.mercury.mercury.rest.reservation;

import de.qaware.mercury.mercury.rest.reservation.dto.ReservationDto;
import de.qaware.mercury.mercury.rest.reservation.dto.SlotDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {

    @PostMapping(path = "/{shopId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createReservation(@PathVariable("shopId") String shopId, @RequestBody ReservationDto reservationDetails) {

    }

    @GetMapping(path = "/{shopId}/slots")
    List<SlotDto> getSlotsForShop(@PathVariable("shopId") String shopId) {
        return new ArrayList<>();
    }

    /**
     * TODO: Move to own controller
     */
    @GetMapping(path = "/{shopId}")
    List<ReservationDto> getExistingReservations(@PathVariable String shopId) {
        return new ArrayList<>();
    }

}
