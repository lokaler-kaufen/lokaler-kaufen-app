package de.qaware.mercury.mercury.rest.location;

import de.qaware.mercury.mercury.business.location.GeoLocationLookup;
import de.qaware.mercury.mercury.business.location.GeoLocationSuggestion;
import de.qaware.mercury.mercury.rest.location.dto.response.LocationSuggestionListDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@RequestMapping(value = "/api/location", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
class LocationController {
    private final GeoLocationLookup geoLocationLookup;

    @GetMapping(path = "/suggestions")
    LocationSuggestionListDto getSuggestions(@RequestParam @NotBlank String query) {
        List<GeoLocationSuggestion> result = geoLocationLookup.search(query);
        return LocationSuggestionListDto.of(result);
    }
}
