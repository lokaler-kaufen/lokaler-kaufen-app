package de.qaware.mercury.mercury.rest.location.dto;

import de.qaware.mercury.mercury.business.location.GeoLocationSuggestion;
import de.qaware.mercury.mercury.util.Lists;
import lombok.Value;

import java.util.List;

@Value
public class LocationSuggestionListDto {
    List<LocationSuggestionDto> locationSuggestions;

    public static LocationSuggestionListDto of(List<GeoLocationSuggestion> suggestions) {
        return new LocationSuggestionListDto(Lists.map(suggestions, LocationSuggestionDto::of));
    }

    @Value
    public static class LocationSuggestionDto {
        String countryCode;
        String zipCode;
        String placeName;
        String adminName1;
        String adminName2;
        String adminName3;

        public static LocationSuggestionDto of(GeoLocationSuggestion g) {
            return new LocationSuggestionDto(
                g.getCountryCode(),
                g.getZipCode(),
                g.getPlaceName(),
                g.getAdminName1(),
                g.getAdminName2(),
                g.getAdminName3()
            );
        }
    }
}
