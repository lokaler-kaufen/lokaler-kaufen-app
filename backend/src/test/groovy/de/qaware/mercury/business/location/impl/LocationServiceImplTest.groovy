package de.qaware.mercury.business.location.impl

import de.qaware.mercury.business.location.GeoLocation
import de.qaware.mercury.business.location.LocationService
import de.qaware.mercury.business.location.LocationSuggestion
import de.qaware.mercury.storage.location.LocationRepository
import spock.lang.Specification
import spock.lang.Subject

class LocationServiceImplTest extends Specification {
    LocationRepository repository = Mock()

    @Subject
    LocationService locationService = new LocationServiceImpl(repository)

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
        data << new LocationSuggestion('id1', 'DE', '83024', 'Rosenheim')
        data << new LocationSuggestion('id2', 'EN', 'X8302', 'London')

        when:
        List<LocationSuggestion> suggestions = locationService.suggest('8302')

        then:
        1 * repository.suggest('8302%', LocationServiceImpl.MAXIMUM_SUGGESTION_COUNT) >> data
        1 * repository.suggest('%8302%', LocationServiceImpl.MAXIMUM_SUGGESTION_COUNT) >> data
        suggestions.size() == 2
    }

    def "Duplicate cities are deduplicated"() {
        given:
        List<LocationSuggestion> data = []
        data << new LocationSuggestion('id1', 'DE', '83024', 'Rosenheim')
        data << new LocationSuggestion('id2', 'DE', '83024', 'Rosenheim')

        when:
        List<LocationSuggestion> suggestions = locationService.suggest('8302')

        then:
        _ * repository.suggest(_, LocationServiceImpl.MAXIMUM_SUGGESTION_COUNT) >> data
        suggestions.size() == 1
    }

    def "Identical cities are only deduplicated if the have the same ZIP code"() {
        given:
        List<LocationSuggestion> data = []
        data << new LocationSuggestion('id1', 'DE', '83024', 'Rosenheim')
        data << new LocationSuggestion('id2', 'DE', '83025', 'Rosenheim')

        when:
        List<LocationSuggestion> suggestions = locationService.suggest('8302')

        then:
        _ * repository.suggest(_, LocationServiceImpl.MAXIMUM_SUGGESTION_COUNT) >> data
        suggestions.size() == 2
    }

    def "Identical ZIP codes are only deduplicated if the have the same city"() {
        given:
        List<LocationSuggestion> data = []
        data << new LocationSuggestion('id1', 'DE', '83024', 'Rosenheim A')
        data << new LocationSuggestion('id2', 'DE', '83024', 'Rosenheim B')

        when:
        List<LocationSuggestion> suggestions = locationService.suggest('8302')

        then:
        _ * repository.suggest(_, LocationServiceImpl.MAXIMUM_SUGGESTION_COUNT) >> data
        suggestions.size() == 2
    }

    def "Suggestions for complete ZIP codes return all results"() {
        given:
        List<LocationSuggestion> dataWildCardEnd = []
        dataWildCardEnd << new LocationSuggestion('id1', 'DE', '83024', 'Rosenheim unique 1')
        dataWildCardEnd << new LocationSuggestion('id2', 'DE', '83024', 'Rosenheim unique 2')
        dataWildCardEnd << new LocationSuggestion('id3', 'DE', '83024', 'Rosenheim unique 3')
        dataWildCardEnd << new LocationSuggestion('id4', 'DE', '83024', 'Rosenheim unique 4')
        dataWildCardEnd << new LocationSuggestion('id5', 'DE', '83024', 'Rosenheim unique 5')
        dataWildCardEnd << new LocationSuggestion('id6', 'DE', '83024', 'Rosenheim unique 6')
        dataWildCardEnd << new LocationSuggestion('id7', 'DE', '83024', 'Rosenheim unique 7')

        when:
        List<LocationSuggestion> suggestions = locationService.suggest('83024')

        then:
        1 * repository.suggest(_) >> dataWildCardEnd
        suggestions.size() == 7
    }
}
