package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.util.Lists;
import de.qaware.mercury.util.Null;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.EnumMap;
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
    @Column(nullable = false)
    private boolean enabled;

    @Setter
    @Column(nullable = false)
    private boolean approved;

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

    @JoinColumn(name = "post_id")
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactEntity> contacts;

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
            shop.isEnabled(),
            shop.isApproved(),
            shop.getGeoLocation().getLatitude(),
            shop.getGeoLocation().getLongitude(),
            shop.getDetails(),
            shop.getWebsite(),
            Lists.map(shop.getContacts(), ContactEntity::of),
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
            Lists.map(contacts, ContactEntity::toContact),
            enabled,
            approved,
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
