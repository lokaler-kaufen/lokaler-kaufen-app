package de.qaware.mercury.mercury.business.reservation;

import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;

import java.util.List;

public interface ReservationService {
    List<Slot> listSlots(Shop shop);

    void createReservation(Shop shop, Slot.Id slotId, ContactType contactType, String contact, String name, String email);
}
