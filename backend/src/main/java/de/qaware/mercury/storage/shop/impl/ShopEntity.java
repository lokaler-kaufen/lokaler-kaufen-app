package de.qaware.mercury.storage.shop.impl;

import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.shop.ContactType;
import de.qaware.mercury.business.shop.DayConfig;
import de.qaware.mercury.business.shop.Shop;
import de.qaware.mercury.business.shop.SlotConfig;
import de.qaware.mercury.util.Sets;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
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
    @OneToMany(mappedBy = "shop", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShopContactEntity> contacts;

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
        ShopEntity entity = new ShopEntity();
        entity.id = shop.getId().getId();
        entity.setName(shop.getName());
        entity.setOwnerName(shop.getOwnerName());
        entity.setEmail(shop.getEmail());
        entity.setStreet(shop.getStreet());
        entity.setZipCode(shop.getZipCode());
        entity.setCity(shop.getCity());
        entity.setAddressSupplement(shop.getAddressSupplement());
        entity.setEnabled(shop.isEnabled());
        entity.setApproved(shop.isApproved());
        entity.setLatitude(shop.getGeoLocation().getLatitude());
        entity.setLongitude(shop.getGeoLocation().getLongitude());
        entity.setDetails(shop.getDetails());
        entity.setWebsite(shop.getWebsite());
        entity.setTimePerSlot(shop.getSlotConfig().getTimePerSlot());
        entity.setTimeBetweenSlots(shop.getSlotConfig().getTimeBetweenSlots());
        if (shop.getSlotConfig().getMonday() != null) {
            entity.setMondayStart(shop.getSlotConfig().getMonday().getStart());
            entity.setMondayStart(shop.getSlotConfig().getMonday().getEnd());
        }
        if (shop.getSlotConfig().getTuesday() != null) {
            entity.setTuesdayStart(shop.getSlotConfig().getTuesday().getStart());
            entity.setTuesdayStart(shop.getSlotConfig().getTuesday().getEnd());
        }
        if (shop.getSlotConfig().getWednesday() != null) {
            entity.setWednesdayStart(shop.getSlotConfig().getWednesday().getStart());
            entity.setWednesdayStart(shop.getSlotConfig().getWednesday().getEnd());
        }
        if (shop.getSlotConfig().getThursday() != null) {
            entity.setThursdayStart(shop.getSlotConfig().getThursday().getStart());
            entity.setThursdayStart(shop.getSlotConfig().getThursday().getEnd());
        }
        if (shop.getSlotConfig().getFriday() != null) {
            entity.setFridayStart(shop.getSlotConfig().getFriday().getStart());
            entity.setFridayStart(shop.getSlotConfig().getFriday().getEnd());
        }
        if (shop.getSlotConfig().getSaturday() != null) {
            entity.setSaturdayStart(shop.getSlotConfig().getSaturday().getStart());
            entity.setSaturdayStart(shop.getSlotConfig().getSaturday().getEnd());
        }
        if (shop.getSlotConfig().getSunday() != null) {
            entity.setSundayStart(shop.getSlotConfig().getSunday().getStart());
            entity.setSundayStart(shop.getSlotConfig().getSunday().getEnd());
        }
        entity.created = shop.getCreated();
        entity.updated = shop.getUpdated();

        entity.setContacts(Sets.map(shop.getContacts(), c -> ShopContactEntity.of(c, entity)));
        return entity;
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
            Sets.map(contacts, ShopContactEntity::toShopContact),
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
