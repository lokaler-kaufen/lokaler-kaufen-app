package de.qaware.mercury.business.shop.impl

import de.qaware.mercury.business.email.EmailService
import de.qaware.mercury.business.location.GeoLocation
import de.qaware.mercury.business.location.LocationService
import de.qaware.mercury.business.login.ShopLoginService
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.ShopNotFoundException
import de.qaware.mercury.business.shop.ShopService
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.shop.ShopRepository
import spock.lang.PendingFeature
import spock.lang.Specification

class ShopServiceImplSpec extends Specification {

    ShopService shopService

    UUIDFactory uuidFactory = Mock()
    LocationService locationService = Mock()
    ShopRepository shopRepository = Mock()
    EmailService emailService = Mock()
    ShopLoginService shopLoginService = Mock()
    Clock clock = Mock()
    ShopServiceConfigurationProperties config = Mock()

    void setup() {
        shopService = new ShopServiceImpl(uuidFactory, locationService, shopRepository, emailService, shopLoginService, clock, config)
    }

    def "List all shops"() {
        when:
        def all = shopService.listAll()

        then:
        1 * shopRepository.listAll() >> [new Shop.ShopBuilder().build()]
        all.size() == 1
    }

    def "Find nearby shops"() {
        setup:
        def location = GeoLocation.of(0.0, 0.0)

        when:
        def nearby = shopService.findNearby('83024')

        then:
        1 * locationService.lookup('83024') >> location
        1 * shopRepository.findNearby(location) >> [new Shop.ShopBuilder().build()]
        nearby.size() == 1
    }

    def "Delete shop by ID"() {
        given:
        def id = Shop.Id.of(UUID.randomUUID())

        when:
        shopService.delete(id)

        then:
        1 * shopRepository.findById(id) >> new Shop.ShopBuilder().build()
        1 * shopRepository.deleteById(id)
        noExceptionThrown()
    }

    def "Delete unknown shop by ID"() {
        given:
        def id = Shop.Id.of(UUID.randomUUID())

        when:
        shopService.delete(id)

        then:
        1 * shopRepository.findById(id) >> null
        thrown ShopNotFoundException
    }

    def "Find shop by ID"() {
        given:
        def id = Shop.Id.of(UUID.randomUUID())
        def shop = new Shop.ShopBuilder().id(id).build()

        when:
        def found = shopService.findById(id)

        then:
        1 * shopRepository.findById(id) >> shop
        found == shop
    }

    def "Can't find unknown shop by ID"() {
        given:
        def id = Shop.Id.of(UUID.randomUUID())

        when:
        def found = shopService.findById(id)

        then:
        1 * shopRepository.findById(id) >> null
        found == null
    }

    def "Find shop by ID (with Exception)"() {
        given:
        def id = Shop.Id.of(UUID.randomUUID())
        def shop = new Shop.ShopBuilder().id(id).build()

        when:
        def found = shopService.findByIdOrThrow(id)

        then:
        1 * shopRepository.findById(id) >> shop
        found == shop
        noExceptionThrown()
    }

    def "Can't find unknown shop by ID (with Exception)"() {
        given:
        def id = Shop.Id.of(UUID.randomUUID())

        when:
        shopService.findByIdOrThrow(id)

        then:
        1 * shopRepository.findById(id) >> null
        thrown ShopNotFoundException
    }


    @PendingFeature
    def "Create"() {
        // TODO Implement me
    }

    @PendingFeature
    def "Update"() {
        // TODO Implement me
    }

    @PendingFeature
    def "ChangeEnabled"() {
        // TODO Implement me
    }

    @PendingFeature
    def "SendCreateLink"() {
        // TODO Implement me
    }

    @PendingFeature
    def "FindByName"() {
        // TODO Implement me
    }

    @PendingFeature
    def "Search"() {
        // TODO Implement me
    }
}
