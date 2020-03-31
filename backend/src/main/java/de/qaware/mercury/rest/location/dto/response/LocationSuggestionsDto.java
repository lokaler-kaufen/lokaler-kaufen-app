package de.qaware.mercury.rest.location.dto.response;

import de.qaware.mercury.business.location.LocationSuggestion;
import de.qaware.mercury.util.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationSuggestionsDto {
    List<LocationSuggestionDto> suggestions;

    public static LocationSuggestionsDto of(List<LocationSuggestion> suggestions) {
        return new LocationSuggestionsDto(Lists.map(suggestions, LocationSuggestionDto::of));
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationSuggestionDto {
        String id;
        String countryCode;
        String zipCode;
        String placeName;

        public static LocationSuggestionDto of(LocationSuggestion g) {
            return new LocationSuggestionDto(
                g.getId(),
                g.getCountryCode(),
                g.getZipCode(),
                g.getPlaceName()
            );
        }
    }
}
