package de.qaware.mercury.mercury.business.reservation;

import de.qaware.mercury.mercury.business.shop.Shop;

import java.util.List;

public interface ReservationService {
    List<Slot> listSlots(Shop shop);
}
