package de.qaware.mercury.mercury.rest.location;

import de.qaware.mercury.mercury.business.location.LocationService;
import de.qaware.mercury.mercury.business.location.LocationSuggestion;
import de.qaware.mercury.mercury.rest.location.dto.response.LocationSuggestionsDto;
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
    private final LocationService locationService;

    @GetMapping(path = "/suggestion")
    LocationSuggestionsDto getSuggestions(@RequestParam @NotBlank String zipCode) {
        List<LocationSuggestion> result = locationService.suggest(zipCode);
        return LocationSuggestionsDto.of(result);
    }
}
