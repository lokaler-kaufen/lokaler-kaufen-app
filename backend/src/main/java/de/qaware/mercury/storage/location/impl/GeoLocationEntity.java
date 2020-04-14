package de.qaware.mercury.storage.location.impl;

import de.qaware.mercury.business.location.GeoLocation;
import de.qaware.mercury.business.location.LocationSuggestion;
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

@Entity
@Getter
// See https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "geolocation")
class GeoLocationEntity {
    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @Setter
    @Column(nullable = false)
    private String countryCode;

    @Setter
    @Column(nullable = false)
    private String zipCode;

    @Setter
    @Column(nullable = false)
    private String placeName;

    @Setter
    @Column(name = "admin_code1", nullable = false)
    private String stateShort;

    @Setter
    @Column(nullable = false)
    private double latitude;

    @Setter
    @Column(nullable = false)
    private double longitude;

    @Setter
    @Column(nullable = false)
    private int accuracy;

    public GeoLocation toGeoLocation() {
        return GeoLocation.of(latitude, longitude);
    }

    public LocationSuggestion toLocationSuggestion() {
        return new LocationSuggestion(
            id == null ? "null" : Long.toString(id),
            countryCode,
            zipCode,
            placeName
        );
    }
}
