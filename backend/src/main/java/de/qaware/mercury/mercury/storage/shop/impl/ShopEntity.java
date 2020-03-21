package de.qaware.mercury.mercury.storage.shop.impl;

import de.qaware.mercury.mercury.business.location.Location;
import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Locale;
import java.util.UUID;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop")
class ShopEntity {
    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
    @Column(nullable = false)
    private boolean enabled;

    @Setter
    @Column(nullable = false)
    private String locationName;

    @Setter
    @Column(nullable = false)
    private double latitude;

    @Setter
    @Column(nullable = false)
    private double longitude;

    public static ShopEntity of(Shop shop) {
        return new ShopEntity(shop.getId().getId(), shop.getName(), shop.isEnabled(),
            shop.getLocation().getName(), shop.getLocation().getLatitude(), shop.getLocation().getLongitude());
    }

    public Shop toShop() {
        return new Shop(Shop.Id.of(id), name, enabled, new Location(locationName, latitude, longitude));
    }
}
