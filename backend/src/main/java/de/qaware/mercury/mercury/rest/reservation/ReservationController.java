package de.qaware.mercury.mercury.rest.reservation;

import de.qaware.mercury.mercury.business.reservation.ReservationService;
import de.qaware.mercury.mercury.business.reservation.Slot;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.rest.reservation.dto.CreateReservationDto;
import de.qaware.mercury.mercury.rest.reservation.dto.SlotsDto;
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

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReservationController {
    private final ReservationService reservationService;
    private final ShopService shopService;

    @PostMapping(path = "/{shopId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createReservation(@PathVariable("shopId") String shopId, @RequestBody CreateReservationDto reservationDetails) {

    }

    @GetMapping(path = "/{shopId}/slot")
    SlotsDto getSlotsForShop(@PathVariable("shopId") String shopId) throws ShopNotFoundException {
        Shop shop = shopService.findByIdOrThrow(Shop.Id.parse(shopId));
        List<Slot> slots = reservationService.listSlots(shop);

        return SlotsDto.of(slots);
    }
}
