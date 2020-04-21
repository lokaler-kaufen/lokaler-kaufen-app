package de.qaware.mercury.business.shop.impl

import de.qaware.mercury.business.admin.AdminService
import de.qaware.mercury.business.email.EmailService
import de.qaware.mercury.business.image.Image
import de.qaware.mercury.business.image.ImageService
import de.qaware.mercury.business.location.GeoLocation
import de.qaware.mercury.business.location.LocationService
import de.qaware.mercury.business.login.ShopLoginService
import de.qaware.mercury.business.shop.*
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.shop.ShopBreaksRepository
import de.qaware.mercury.storage.shop.ShopRepository
import de.qaware.mercury.test.fixtures.ShopCreationFixtures
import de.qaware.mercury.test.fixtures.ShopFixtures
import de.qaware.mercury.test.fixtures.ShopUpdateFixtures
import de.qaware.mercury.test.time.TestClock
import de.qaware.mercury.test.uuid.TestUUIDFactory
import spock.lang.Specification

class ShopServiceImplSpec extends Specification {

    ShopService shopService

    UUIDFactory uuidFactory = Mock()
    LocationService locationService = Mock()
    ShopRepository shopRepository = Mock()
    EmailService emailService = Mock()
    ShopLoginService shopLoginService = Mock()
    Clock clock = new TestClock()
    ShopServiceConfigurationProperties config = Mock()
    ShopSharingConfigurationProperties sharingConfig = Mock()
    AdminService adminService = Mock()
    ImageService imageService = Mock()
    ShopBreaksRepository shopBreaksRepository = Mock()
    SlugService slugService = Mock()

    void setup() {
        shopService = new ShopServiceImpl(uuidFactory, locationService, shopRepository, emailService, shopLoginService, clock, config, adminService, imageService, shopBreaksRepository, slugService, sharingConfig)
    }

    def "List all shops"() {
        when:
        List<Shop> all = shopService.listAll()

        then:
        1 * shopRepository.listAll() >> [ShopFixtures.create()]
        all.size() == 1
    }

    def "Find nearby shops"() {
        setup:
        GeoLocation location = GeoLocation.of(0.0, 0.0)

        when:
        List<ShopWithDistance> nearby = shopService.findActive('83024')

        then:
        1 * locationService.lookup('83024') >> location
        1 * shopRepository.findActive() >> [new Shop.ShopBuilder().geoLocation(GeoLocation.of(0.0, 0.0)).build()]
        nearby.size() == 1
    }

    def "Shops outside the search radius are filtered"() {
        setup:
        GeoLocation location = GeoLocation.of(0.5, 0.5)
        // 5 km around our location
        int maxDistance = 5

        // a location more than 5km away from our location (1.5 degrees should be about 15km)
        GeoLocation remoteLocation = GeoLocation.of(2, 2)

        String nameOfShopWithin = "within"


        when:
        List<ShopWithDistance> nearby = shopService.findActive('83024', maxDistance)

        then:
        1 * locationService.lookup('83024') >> location

        // one shop within our radius, one outside
        1 * shopRepository.findActive(_) >> [new Shop.ShopBuilder().geoLocation(location).name(nameOfShopWithin).build(), new Shop.ShopBuilder().geoLocation(remoteLocation).build()]

        // we should just get one
        nearby.size() == 1

        // we get the right one back
        nearby.get(0).shop.name == nameOfShopWithin
    }

    def "Delete shop by ID"() {
        given:
        Shop.Id id = Shop.Id.of(UUID.randomUUID())

        when:
        shopService.delete(id)

        then:
        1 * shopRepository.findById(id) >> ShopFixtures.create()
        1 * shopRepository.deleteById(id)
        noExceptionThrown()
    }

    def "Delete unknown shop by ID"() {
        given:
        Shop.Id id = Shop.Id.of(UUID.randomUUID())

        when:
        shopService.delete(id)

        then:
        1 * shopRepository.findById(id) >> null
        thrown ShopNotFoundException
    }

    def "Find shop by ID"() {
        given:
        Shop.Id id = Shop.Id.of(UUID.randomUUID())
        Shop shop = ShopFixtures.create()

        when:
        Shop found = shopService.findById(id)

        then:
        1 * shopRepository.findById(id) >> shop
        found == shop
    }

    def "Can't find unknown shop by ID"() {
        given:
        Shop.Id id = Shop.Id.of(UUID.randomUUID())

        when:
        Shop found = shopService.findById(id)

        then:
        1 * shopRepository.findById(id) >> null
        found == null
    }

    def "Find shop by ID (with Exception)"() {
        given:
        Shop shop = ShopFixtures.create()

        when:
        Shop found = shopService.findByIdOrThrow(shop.id)

        then:
        1 * shopRepository.findById(shop.id) >> shop
        found == shop
        noExceptionThrown()
    }

    def "Can't find unknown shop by ID (with Exception)"() {
        given:
        Shop.Id id = Shop.Id.of(UUID.randomUUID())

        when:
        shopService.findByIdOrThrow(id)

        then:
        1 * shopRepository.findById(id) >> null
        thrown ShopNotFoundException
    }


    def "Create a new shop"() {
        given:
        UUID uuid = UUID.randomUUID()
        GeoLocation location = GeoLocation.of(0.0, 0.0)
        ShopCreation creation = ShopCreationFixtures.create()

        when:
        Shop shop = shopService.create(creation)

        then:
        1 * shopLoginService.hasLogin(creation.email) >> false
        1 * uuidFactory.create() >> uuid
        1 * locationService.lookup(creation.zipCode) >> location

        and:
        with(shop) {
            shop.id.id == uuid
            shop.email == creation.email
            shop.name == creation.name
            shop.geoLocation == location
            shop.updated == clock.nowZoned()
            shop.created == clock.nowZoned()
        }
    }

    def "Can't create an existing shop"() {
        given:
        ShopCreation creation = ShopCreationFixtures.create()

        when:
        shopService.create(creation)

        then:
        1 * shopLoginService.hasLogin(creation.email) >> true
        thrown ShopAlreadyExistsException
    }

    def "Update shop"() {
        given:
        ShopUpdate update = ShopUpdateFixtures.create()
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = new Shop.ShopBuilder().id(shopId).zipCode('83024').build()

        when:
        Shop updated = shopService.update(shop, update)

        then:
        1 * shopRepository.update(_)

        and:
        with(updated) {
            id == shopId
            zipCode == update.zipCode
        }
    }

    def "Can't change unknown shop"() {
        given:
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())

        when:
        shopService.changeApproved(shopId, true)

        then:
        1 * shopRepository.findById(shopId) >> null
        thrown ShopNotFoundException
    }

    def "Approve shop"() {
        given:
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = new Shop.ShopBuilder().id(shopId).approved(false).build()

        when:
        shopService.changeApproved(shopId, true)

        then:
        1 * shopRepository.findById(shopId) >> shop
        1 * shopRepository.update({ Shop s -> s.approved })
    }

    def "Does nothing if already approved"() {
        given:
        Shop shop = ShopFixtures.create()

        when:
        shopService.changeApproved(shop.id, true)

        then:
        1 * shopRepository.findById(_) >> shop
        0 * shopRepository.update(_)
    }

    def "Disapproval sends email"() {
        given:
        Shop shop = ShopFixtures.create()

        when:
        shopService.changeApproved(shop.id, false)

        then:
        1 * shopRepository.findById(shop.id) >> shop
        1 * shopRepository.update({ Shop s -> !s.approved })
        1 * emailService.sendShopApprovalRevoked(shop)
    }

    def "Adds image to shop"() {
        setup:
        TestUUIDFactory testUUIDFactory = new TestUUIDFactory()
        Image newImage = new Image(Image.Id.random(testUUIDFactory))
        Image.Id oldImage = Image.Id.random(uuidFactory)
        Shop shop = ShopFixtures.create(uuidFactory, oldImage, clock)

        when:
        shopService.setImage(shop, newImage, "#FFFFFF")

        then:
        // Delete old image
        1 * imageService.deleteImage(oldImage)
        // Update the shop to the new id
        1 * shopRepository.update(_) >> { Shop updatedShop ->
            updatedShop.getImageId() == newImage.id
        }
    }

    def "Send create link to email"() {
        when:
        shopService.sendCreateLink('test@lokaler.kaufen')

        then:
        shopLoginService.hasLogin('test@lokaler.kaufen') >> false
        emailService.sendShopCreationLink('test@lokaler.kaufen')
    }

    def "Find by name"() {
        given:
        Shop shop = ShopFixtures.create()

        when:
        List<Shop> found = shopService.findByName('Test Shop')

        then:
        1 * shopRepository.findByName('Test Shop') >> [shop]
        found.size() == 1
        found[0] == shop
    }

    def "Search by query and ZIP code"() {
        given:
        GeoLocation location = GeoLocation.of(0.0, 0.0)

        when:
        List<ShopWithDistance> results = shopService.searchActive('*', '83024')

        then:
        1 * locationService.lookup('83024') >> location
        1 * shopRepository.searchActive('*') >> [new Shop.ShopBuilder().geoLocation(GeoLocation.of(0, 0)).build()]
        results.size() == 1
    }

    def "Search with max distance only provides shops within radius"() {
        given:
        // our location
        GeoLocation location = GeoLocation.of(0.5, 0.5)

        // a location outside our search radius
        GeoLocation remoteLocation = GeoLocation.of(2.0, 2.0)

        // our search radius in km
        int maxDistance = 5
        String nameOfShopWithin = "within"

        when:
        List<ShopWithDistance> results = shopService.searchActive('*', '83024', maxDistance)

        then:
        1 * locationService.lookup('83024') >> location
        // one shop within, one outside our search radius
        1 * shopRepository.searchActive('*', _) >> [new Shop.ShopBuilder().geoLocation(location).name(nameOfShopWithin).build(), new Shop.ShopBuilder().geoLocation(remoteLocation).build()]

        // we only get one shop back
        results.size() == 1

        // we get the right one back
        results.get(0).shop.name == nameOfShopWithin
    }
}
