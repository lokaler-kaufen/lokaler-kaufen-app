package de.qaware.mercury.rest.reservation;

import de.qaware.mercury.business.location.FederalState;
import de.qaware.mercury.business.location.LocationService;
import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import de.qaware.mercury.business.login.LoginException;
import de.qaware.mercury.business.login.ReservationCancellationToken;
import de.qaware.mercury.business.reservation.HolidayService;
import de.qaware.mercury.business.reservation.ReservationFailedException;
import de.qaware.mercury.business.reservation.ReservationNotFoundException;
import de.qaware.mercury.business.reservation.ReservationService;
import de.qaware.mercury.business.reservation.Slot;
import de.qaware.mercury.business.reservation.SlotService;
import de.qaware.mercury.business.reservation.Slots;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.ShopNotFoundException;
import de.qaware.mercury.business.shop.ShopService;
import de.qaware.mercury.business.validation.Validation;
import de.qaware.mercury.rest.reservation.dto.request.CreateReservationDto;
import de.qaware.mercury.rest.reservation.dto.response.SlotsDto;
import de.qaware.mercury.rest.shop.dto.request.SlotConfigDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/reservation", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@SuppressWarnings("java:S4784") // JDK since 9 has additional protection against ReDos attacks
public class ReservationController {
    private final ReservationService reservationService;
    private final ShopService shopService;
    private final SlotService slotService;
    private final HolidayService holidayService;
    private final LocationService locationService;

    @PostMapping(path = "/{shopId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void createReservation(@PathVariable("shopId") @Pattern(regexp = Validation.SHOP_ID) String shopId, @Valid @RequestBody CreateReservationDto request) throws ShopNotFoundException, ReservationFailedException {
        Shop shop = shopService.findByIdOrThrow(Shop.Id.parse(shopId));
        reservationService.createReservation(
            shop, Slot.Id.parse(request.getSlotId()), ContactType.parse(request.getContactType()),
            request.getContact(), request.getName(), request.getEmail()
        );
    }

    @DeleteMapping
    public void cancelReservation(@RequestParam @NotBlank String token) throws ReservationNotFoundException, LoginException, ShopNotFoundException {
        reservationService.cancelReservation(ReservationCancellationToken.of(token));
    }

    @GetMapping(path = "/{shopId}/slot")
    public SlotsDto getSlotsForShop(
        @PathVariable("shopId") @Pattern(regexp = Validation.SHOP_ID) String shopId,
        @RequestParam(value = "days", defaultValue = "7") @Min(1) int days
    ) throws ShopNotFoundException, LocationNotFoundException {
        Shop shop = shopService.findByIdOrThrow(Shop.Id.parse(shopId));
        Slots slots = reservationService.listSlots(shop, days);
        FederalState federalState = locationService.resolveFederalState(shop.getZipCode());

        return SlotsDto.of(slots, date -> holidayService.isHoliday(date, federalState));
    }

    @PostMapping(path = "/preview", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SlotsDto previewSlots(@Valid @RequestBody SlotConfigDto request) {
        Slots slots = slotService.previewSlots(request.toSlots());
        // The date -> false stuff below marks every day as a non-holiday
        return SlotsDto.of(slots, date -> false);
    }
}
