package de.qaware.mercury.mercury.rest.reservation;

import de.qaware.mercury.mercury.business.reservation.ReservationService;
import de.qaware.mercury.mercury.business.reservation.Slot;
import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.InvalidContactTypeException;
import de.qaware.mercury.mercury.business.shop.InvalidShopIdException;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.mercury.business.shop.ShopService;
import de.qaware.mercury.mercury.rest.reservation.dto.request.CreateReservationDto;
import de.qaware.mercury.mercury.rest.reservation.dto.response.SlotsDto;
import de.qaware.mercury.mercury.util.validation.GuidValidation;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class ReservationController {
    private final ReservationService reservationService;
    private final ShopService shopService;

    @PostMapping(path = "/{shopId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    void createReservation(@PathVariable("shopId") @Pattern(regexp = GuidValidation.REGEX) String shopId, @Valid @RequestBody CreateReservationDto request) throws ShopNotFoundException, InvalidShopIdException, InvalidContactTypeException {
        Shop shop = shopService.findByIdOrThrow(Shop.Id.parse(shopId));
        reservationService.createReservation(
            shop, Slot.Id.parse(request.getSlotId()), ContactType.parse(request.getContactType()),
            request.getContact(), request.getName(), request.getEmail()
        );
    }

    @GetMapping(path = "/{shopId}/slot")
    SlotsDto getSlotsForShop(@PathVariable("shopId") @Pattern(regexp = GuidValidation.REGEX) String shopId) throws ShopNotFoundException {
        Shop shop = shopService.findByIdOrThrow(Shop.Id.parse(shopId));
        List<Slot> slots = reservationService.listSlots(shop);

        return SlotsDto.of(slots);
    }
}
