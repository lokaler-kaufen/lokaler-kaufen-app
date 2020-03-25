package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.util.Null;
import de.qaware.mercury.util.Sets;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.springframework.lang.Nullable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.EnumMap;
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
    @Type(type = "com.vladmihalcea.hibernate.type.array.StringArrayType")
    @Column(nullable = false)
    private String[] contactTypes;

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

    @Column(nullable = false)
    private ZonedDateTime created;

    @Column(nullable = false)
    private ZonedDateTime updated;

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
            Sets.map(shop.getContactTypes().keySet(), ContactType::name).toArray(new String[0]), // TODO MKA: Store contact types with contact info
            shop.isEnabled(),
            shop.getGeoLocation().getLatitude(),
            shop.getGeoLocation().getLongitude(),
            shop.getDetails(),
            shop.getWebsite(),
            shop.getSlotConfig().getTimePerSlot(),
            shop.getSlotConfig().getTimeBetweenSlots(),
            Null.map(shop.getSlotConfig().getMonday(), DayConfig::getStart),
            Null.map(shop.getSlotConfig().getMonday(), DayConfig::getEnd),
            Null.map(shop.getSlotConfig().getTuesday(), DayConfig::getStart),
            Null.map(shop.getSlotConfig().getTuesday(), DayConfig::getEnd),
            Null.map(shop.getSlotConfig().getWednesday(), DayConfig::getStart),
            Null.map(shop.getSlotConfig().getWednesday(), DayConfig::getEnd),
            Null.map(shop.getSlotConfig().getThursday(), DayConfig::getStart),
            Null.map(shop.getSlotConfig().getThursday(), DayConfig::getEnd),
            Null.map(shop.getSlotConfig().getFriday(), DayConfig::getStart),
            Null.map(shop.getSlotConfig().getFriday(), DayConfig::getEnd),
            Null.map(shop.getSlotConfig().getSaturday(), DayConfig::getStart),
            Null.map(shop.getSlotConfig().getSaturday(), DayConfig::getEnd),
            Null.map(shop.getSlotConfig().getSunday(), DayConfig::getStart),
            Null.map(shop.getSlotConfig().getSunday(), DayConfig::getEnd),
            shop.getCreated(),
            shop.getUpdated()
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
            GeoLocation.of(latitude, longitude),
            details,
            website,
            new SlotConfig(
                timePerSlot, timeBetweenSlots,
                loadSlot(this::getMondayStart, this::getMondayEnd),
                loadSlot(this::getTuesdayStart, this::getTuesdayEnd),
                loadSlot(this::getWednesdayStart, this::getWednesdayEnd),
                loadSlot(this::getThursdayStart, this::getThursdayEnd),
                loadSlot(this::getFridayStart, this::getFridayEnd),
                loadSlot(this::getSaturdayStart, this::getSaturdayEnd),
                loadSlot(this::getSundayStart, this::getSundayEnd)
            ),
            created,
            updated
        );
    }

    private DayConfig loadSlot(Supplier<LocalTime> start, Supplier<LocalTime> end) {
        LocalTime startValue = start.get();
        LocalTime endValue = end.get();

        if (startValue == null || endValue == null) {
            return null;
        }

        return new DayConfig(startValue, endValue);
    }

    private Map<ContactType, String> fakeMap(String[] contactTypes) {
        Map<ContactType, String> result = new EnumMap<>(ContactType.class);
        for (String contactType : contactTypes) {
            result.put(ContactType.parse(contactType), "");
        }
        return result;
    }
}
