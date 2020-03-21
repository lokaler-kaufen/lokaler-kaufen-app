package de.qaware.mercury.mercury.storage.shop.impl;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import de.qaware.mercury.mercury.business.shop.Slot;
import de.qaware.mercury.mercury.business.shop.Slots;
import de.qaware.mercury.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop")
public class ShopEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private String ownerName;

    @Setter
    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String street;

    @Setter
    @Column(nullable = false)
    private String zipCode;

    @Setter
    @Column(nullable = false)
    private String city;

    @Setter
    @Column(nullable = false)
    private String addressSupplement;

    @Setter
    @Type(
        type = "com.vladmihalcea.hibernate.type.array.ListArrayType",
        parameters = {
            @Parameter(
                name = ListArrayType.SQL_ARRAY_TYPE,
                value = "contact_type"
            )
        }
    )
    @Column(nullable = false, columnDefinition = "contact_type[]")
    private List<ContactType> contactTypes;

    @Setter
    @Column(nullable = false)
    private boolean enabled;

    @Setter
    @Column(nullable = false)
    private double latitude;

    @Setter
    @Column(nullable = false)
    private double longitude;

    @Setter
    @Column(nullable = false)
    private String details;

    @Setter
    @Column(nullable = true)
    @Nullable
    private String website;

    @Setter
    @Column(nullable = false)
    private int timePerSlot;

    @Setter
    @Column(nullable = false)
    private int timeBetweenSlots;

    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime mondayStart;
    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime mondayEnd;

    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime tuesdayStart;
    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime tuesdayEnd;

    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime wednesdayStart;
    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime wednesdayEnd;

    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime thursdayStart;
    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime thursdayEnd;

    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime fridayStart;
    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime fridayEnd;

    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime saturdayStart;
    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime saturdayEnd;

    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime sundayStart;
    @Setter
    @Column(nullable = true, columnDefinition = "varchar")
    private LocalTime sundayEnd;

    public static ShopEntity of(Shop shop) {
        return new ShopEntity(
            shop.getId().getId(),
            shop.getName(),
            shop.getOwnerName(),
            shop.getEmail(),
            shop.getStreet(),
            shop.getZipCode(),
            shop.getCity(),
            shop.getAddressSupplement(),
            new ArrayList<>(shop.getContactTypes().keySet()), // TODO MKA: Store contact types with contact info
            shop.isEnabled(),
            shop.getGeoLocation().getLatitude(),
            shop.getGeoLocation().getLongitude(),
            shop.getDetails(),
            shop.getWebsite(),
            shop.getSlots().getTimePerSlot(),
            shop.getSlots().getTimeBetweenSlots(),
            Null.map(shop.getSlots().getMonday(), Slot::getStart),
            Null.map(shop.getSlots().getMonday(), Slot::getEnd),
            Null.map(shop.getSlots().getTuesday(), Slot::getStart),
            Null.map(shop.getSlots().getTuesday(), Slot::getEnd),
            Null.map(shop.getSlots().getWednesday(), Slot::getStart),
            Null.map(shop.getSlots().getWednesday(), Slot::getEnd),
            Null.map(shop.getSlots().getThursday(), Slot::getStart),
            Null.map(shop.getSlots().getThursday(), Slot::getEnd),
            Null.map(shop.getSlots().getFriday(), Slot::getStart),
            Null.map(shop.getSlots().getFriday(), Slot::getEnd),
            Null.map(shop.getSlots().getSaturday(), Slot::getStart),
            Null.map(shop.getSlots().getSaturday(), Slot::getEnd),
            Null.map(shop.getSlots().getSunday(), Slot::getStart),
            Null.map(shop.getSlots().getSunday(), Slot::getEnd)
        );
    }

    public Shop toShop() {
        return new Shop(
            Shop.Id.of(id),
            name,
            ownerName,
            email,
            street,
            zipCode,
            city,
            addressSupplement,
            fakeMap(contactTypes), // TODO MKA: Load contact types with contact info
            enabled,
            new GeoLocation(latitude, longitude),
            details,
            website,
            new Slots(
                timePerSlot, timeBetweenSlots,
                loadSlot(this::getMondayStart, this::getMondayEnd),
                loadSlot(this::getTuesdayStart, this::getTuesdayEnd),
                loadSlot(this::getWednesdayStart, this::getWednesdayEnd),
                loadSlot(this::getThursdayStart, this::getThursdayEnd),
                loadSlot(this::getFridayStart, this::getFridayEnd),
                loadSlot(this::getSaturdayStart, this::getSaturdayEnd),
                loadSlot(this::getSundayStart, this::getSundayEnd)
            )
        );
    }

    private Slot loadSlot(Supplier<LocalTime> start, Supplier<LocalTime> end) {
        LocalTime startValue = start.get();
        LocalTime endValue = end.get();

        if (startValue == null || endValue == null) {
            return null;
        }

        return new Slot(startValue, endValue);
    }

    private Map<ContactType, String> fakeMap(Iterable<ContactType> contactTypes) {
        Map<ContactType, String> result = new HashMap<>();
        for (ContactType contactType : contactTypes) {
            result.put(contactType, "");
        }
        return result;
    }
}
