package de.qaware.mercury.mercury.storage.shop.impl;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import de.qaware.mercury.mercury.business.location.GeoLocation;
import de.qaware.mercury.mercury.business.shop.ContactType;
import de.qaware.mercury.mercury.business.shop.Shop;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shop")
@SqlResultSetMapping(name = "ShopWithDistance", classes = @ConstructorResult(
    targetClass = ShopWithDistance.class,
    columns = {
        @ColumnResult(name = "id", type = UUID.class),
        @ColumnResult(name = "name", type = String.class),
        @ColumnResult(name = "distance", type = Double.class),
        @ColumnResult(name = "contact_types", type = StringArrayType.class),
    })
)
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
            shop.getContactTypes(),
            shop.isEnabled(),
            shop.getGeoLocation().getLatitude(),
            shop.getGeoLocation().getLongitude()
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
            contactTypes,
            enabled,
            new GeoLocation(latitude, longitude)
        );
    }
}
