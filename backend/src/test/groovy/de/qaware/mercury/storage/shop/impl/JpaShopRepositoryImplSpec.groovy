package de.qaware.mercury.storage.shop.impl

import de.qaware.mercury.business.location.BoundingBox
import de.qaware.mercury.business.location.GeoLocation
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.test.fixtures.ShopFixtures
import de.qaware.mercury.test.time.TestClock
import spock.lang.Specification
import spock.lang.Subject

import java.time.ZonedDateTime

class JpaShopRepositoryImplSpec extends Specification {

    ShopDataRepository dataRepository
    Clock clock = new TestClock()

    @Subject
    JpaShopRepositoryImpl repository

    def setup() {
        dataRepository = Mock(ShopDataRepository)
        repository = new JpaShopRepositoryImpl(dataRepository, clock)
    }

    def "Returns all shops"() {
        setup:
        Shop shop = ShopFixtures.create()
        ShopEntity shopEntity = ShopEntity.of(shop)
        List<ShopEntity> entityList = List.of(shopEntity)
        List<Shop> shopList = List.of(shop)

        when:
        List<Shop> result = repository.listAll()

        then:
        result == shopList
        1 * dataRepository.findAll() >> entityList
    }

    def "New shop gets stored"() {
        setup:
        Shop shop = ShopFixtures.create()
        ZonedDateTime now = ZonedDateTime.now()
        clock.nowZoned() >> now

        when:
        repository.insert(shop)

        then:
        1 * dataRepository.save(_) >> { ShopEntity passedShopEntity ->
            ShopEntity.of(shop.withCreated(now)) == passedShopEntity
            passedShopEntity.getCreated() == now
        }
    }

    def "Finds shop by id"() {
        setup:
        Shop shop = ShopFixtures.create()
        ShopEntity shopEntity = ShopEntity.of(shop)

        when:
        Shop result = repository.findById(shop.id)

        then:
        result == shop
        1 * dataRepository.findById(shop.id.getId()) >> Optional.of(shopEntity)
    }

    def "Returns null if shop was not found"() {
        setup:
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())

        when:
        Shop result = repository.findById(shopId)

        then:
        result == null
        1 * dataRepository.findById(shopId.getId()) >> Optional.ofNullable(null)
    }

    def "Finds active shops"() {
        setup:
        Shop shop = ShopFixtures.create()
        ShopEntity shopEntity = ShopEntity.of(shop)
        List<ShopEntity> entityList = List.of(shopEntity)
        List<Shop> shopList = List.of(shop)

        when:
        List<Shop> result = repository.findActive()

        then:
        result == shopList
        1 * dataRepository.findActive() >> entityList
    }

    def "Finds active shops in bounding box"() {
        setup:
        Shop shop = ShopFixtures.create()
        ShopEntity shopEntity = ShopEntity.of(shop)
        List<ShopEntity> entityList = List.of(shopEntity)
        List<Shop> shopList = List.of(shop)
        double shouthWestLatitude = 12, shouthWestLongitude=13
        double northEastLatitude = 14, northEastLongitude=15
        BoundingBox box = new BoundingBox(GeoLocation.of(shouthWestLatitude, shouthWestLongitude), GeoLocation.of(northEastLatitude, northEastLongitude))

        when:
        List<Shop> result = repository.findActive(box)

        then:
        result == shopList
        1 * dataRepository.findActive(northEastLatitude, northEastLongitude, shouthWestLatitude, shouthWestLongitude) >> entityList
    }

    def "Searches active shops"() {
        setup:
        String query = "query"
        Shop shop = ShopFixtures.create()
        ShopEntity shopEntity = ShopEntity.of(shop)
        List<ShopEntity> entityList = List.of(shopEntity)
        List<Shop> shopList = List.of(shop)

        when:
        List<Shop> result = repository.searchActive(query)

        then:
        result == shopList
        1 * dataRepository.searchActive('%' + query + '%') >> entityList
    }

    def "Searches active shops in bounding box"() {
        setup:
        String query = "query"
        Shop shop = ShopFixtures.create()
        ShopEntity shopEntity = ShopEntity.of(shop)
        List<ShopEntity> entityList = List.of(shopEntity)
        List<Shop> shopList = List.of(shop)
        double shouthWestLatitude = 12, shouthWestLongitude=13
        double northEastLatitude = 14, northEastLongitude=15
        BoundingBox box = new BoundingBox(GeoLocation.of(shouthWestLatitude, shouthWestLongitude), GeoLocation.of(northEastLatitude, northEastLongitude))

        when:
        List<Shop> result = repository.searchActive(query, box)

        then:
        result == shopList
        1 * dataRepository.searchActive('%' + query + '%', northEastLatitude, northEastLongitude, shouthWestLatitude, shouthWestLongitude) >> entityList
    }

    def "Updates shop"() {
        setup:
        Shop shop = ShopFixtures.create()
        ZonedDateTime now = ZonedDateTime.now()
        clock.nowZoned() >> now

        when:
        repository.update(shop)

        then:
        1 * dataRepository.save(_) >> { ShopEntity passedShopEntity ->
            ShopEntity.of(shop.withUpdated(now)) == passedShopEntity
            passedShopEntity.getUpdated() == now
        }
    }

    def "Deletes shop by id"() {
        setup:
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())

        when:
        repository.deleteById(shopId)

        then:
        1 * dataRepository.deleteById(shopId.getId())
    }

    def "Find shops by name"() {
        setup:
        String testName = "Test Shopname"
        Shop shop = ShopFixtures.create()
        List<Shop> shopList = List.of(shop)
        ShopEntity shopEntity = ShopEntity.of(shop)
        List<ShopEntity> entityList = List.of(shopEntity)

        when:
        List<Shop> result = repository.findByName(testName)

        then:
        result == shopList
        1 * dataRepository.findByName(testName) >> entityList
    }
}
