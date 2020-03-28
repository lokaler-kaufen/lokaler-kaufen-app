package de.qaware.mercury.rest.location;

import de.qaware.mercury.business.location.LocationService;
import de.qaware.mercury.business.location.LocationSuggestion;
import de.qaware.mercury.business.location.impl.LocationNotFoundException;
import de.qaware.mercury.rest.location.dto.response.LocationSuggestionsDto;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final LocationService locationService;

    @GetMapping(path = "/suggestion")
    public LocationSuggestionsDto getSuggestions(@RequestParam @NotBlank String zipCode) {
        List<LocationSuggestion> result = locationService.suggest(zipCode);
        return LocationSuggestionsDto.of(result);
    }

    @GetMapping
    public ResponseEntity<Void> isLocationKnown(@RequestParam @NotBlank String zipCode) {
        try {
            locationService.lookup(zipCode);
            return ResponseEntity.ok().build();
        } catch (LocationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
