package de.qaware.mercury.business.reservation;

import lombok.Getter;

/**
 * Who cancelled the reservation?
 */
public enum ReservationCancellationSide {
    SHOP("shop"),
    CUSTOMER("customer");

    @Getter
    private final String id;

    ReservationCancellationSide(String id) {
        this.id = id;
    }

    public static ReservationCancellationSide parse(String id) {
        for (ReservationCancellationSide side : values()) {
            if (side.getId().equals(id)) {
                return side;
            }
        }

        throw new IllegalArgumentException(String.format("No CancellationSide enum value with id '%s'", id));
    }
}
