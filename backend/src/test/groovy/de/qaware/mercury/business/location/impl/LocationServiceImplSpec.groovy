package de.qaware.mercury.business.location.impl

import de.qaware.mercury.business.location.GeoLocation
import de.qaware.mercury.business.location.LocationService
import de.qaware.mercury.business.location.LocationSuggestion
import de.qaware.mercury.storage.location.LocationRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification

@ContextConfiguration(classes = LocationServiceImpl)
class LocationServiceImplSpec extends Specification {

    @Autowired
    LocationService locationService

    @SpringBean
    LocationRepository repository = Mock()

    def "Lookup Location by unknown Zipcode"() {
        when:
        locationService.lookup('83024')

        then:
        1 * repository.fromZipCode('83024') >> null
        LocationNotFoundException exception = thrown()
    }

    def "Lookup Location by Zipcode"() {
        setup:
        GeoLocation expected = GeoLocation.of(48.104346, 11.600851)

        when:
        GeoLocation location = locationService.lookup('83024')

        then:
        1 * repository.fromZipCode('83024') >> expected
        location == expected
    }

    def "Empty suggestions for short ZIP code"() {
        when:
        List<LocationSuggestion> suggestions = locationService.suggest('')

        then:
        suggestions.isEmpty()
    }

    def "Suggestions for ZIP code"() {
        setup:
        List<LocationSuggestion> data = []
        data << new LocationSuggestion('DE', '83024', 'Rosenheim')
        data << new LocationSuggestion('EN', 'X8302', 'London')

        when:
        List<LocationSuggestion> suggestions = locationService.suggest('8302')

        then:
        1 * repository.suggest('8302%', LocationServiceImpl.MAXIMUM_SUGGESTION_COUNT) >> data
        1 * repository.suggest('%8302%', LocationServiceImpl.MAXIMUM_SUGGESTION_COUNT) >> data
        suggestions.size() == 2
    }
}
