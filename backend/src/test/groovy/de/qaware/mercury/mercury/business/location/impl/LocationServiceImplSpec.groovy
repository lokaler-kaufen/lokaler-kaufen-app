package de.qaware.mercury.mercury.business.location.impl

import de.qaware.mercury.mercury.business.location.GeoLocation
import de.qaware.mercury.mercury.business.location.LocationService
import de.qaware.mercury.mercury.business.location.LocationSuggestion
import de.qaware.mercury.mercury.storage.location.LocationRepository
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

    def "Lookup Default Location by Zipcode"() {
        when:
        def location = locationService.lookup('83024')

        then:
        1 * repository.fromZipCode('83024') >> null
        location == LocationServiceImpl.DEFAULT_GEOLOCATION
    }

    def "Lookup Location by Zipcode"() {
        setup:
        def expected = GeoLocation.of(48.104346, 11.600851)

        when:
        def location = locationService.lookup('83024')

        then:
        1 * repository.fromZipCode('83024') >> expected
        location == expected
    }

    def "Empty suggestions for short ZIP code"() {
        when:
        def suggestions = locationService.suggest('83')

        then:
        suggestions.isEmpty()
    }

    def "Suggestions for ZIP code"() {
        setup:
        def data = []
        data << new LocationSuggestion('DE', '83024', 'Rosenheim')
        data << new LocationSuggestion('EN', 'X8302', 'London')

        when:
        def suggestions = locationService.suggest('8302')

        then:
        1 * repository.suggest('8302%') >> data
        1 * repository.suggest('%8302%') >> data
        suggestions.size() == 2
    }
}
