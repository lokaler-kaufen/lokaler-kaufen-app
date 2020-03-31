package de.qaware.mercury.storage.shop.impl

import de.qaware.mercury.business.location.BoundingBox
import de.qaware.mercury.business.location.GeoLocation
import de.qaware.mercury.business.shop.ContactType
import de.qaware.mercury.business.shop.DayConfig
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.shop.SlotConfig
import de.qaware.mercury.business.time.Clock
import spock.lang.Specification
import spock.lang.Subject

import java.time.LocalTime
import java.time.ZonedDateTime


class JpaShopRepositoryImplSpec extends Specification {

    ShopDataRepository dataRepository
    Clock clock

    @Subject
    JpaShopRepositoryImpl repository

    def setup() {
        dataRepository = Mock(ShopDataRepository)
        clock = Mock(Clock)
        repository = new JpaShopRepositoryImpl(dataRepository, clock)
    }

    def "Returns all shops"() {
        setup:
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
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
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
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
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
        ShopEntity shopEntity = ShopEntity.of(shop)

        when:
        Shop result = repository.findById(shopId)

        then:
        result == shop
        1 * dataRepository.findById(shopId.getId()) >> Optional.of(shopEntity)
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
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
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
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
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
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
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
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
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
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
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
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = createShopObject(shopId)
        List<Shop> shopList = List.of(shop)
        ShopEntity shopEntity = ShopEntity.of(shop)
        List<ShopEntity> entityList = List.of(shopEntity)

        when:
        List<Shop> result = repository.findByName(testName)

        then:
        result == shopList
        1 * dataRepository.findByName(testName) >> entityList
    }

    private static Shop createShopObject(Shop.Id shopId) {
        return new Shop(
            shopId,
            "Name",
            "Owner Name",
            "info@example.com",
            "Street",
            "23947",
            "City",
            "Address Supplement",
            new HashMap<ContactType, String>(),
            true,
            true,
            GeoLocation.of(47, 12),
            "Details",
            "www.example.com",
            createSlotConfig(),
            createZonedDateTime(),
            createZonedDateTime()
        )
    }

    private static SlotConfig createSlotConfig() {
        return new SlotConfig(
            15,
            15,
            new DayConfig(LocalTime.parse("10:00"), LocalTime.parse("11:00")),
            new DayConfig(LocalTime.parse("11:30"), LocalTime.parse("12:30")),
            new DayConfig(LocalTime.parse("13:00"), LocalTime.parse("14:00")),
            new DayConfig(LocalTime.parse("14:30"), LocalTime.parse("15:30")),
            new DayConfig(LocalTime.parse("16:00"), LocalTime.parse("17:00")),
            new DayConfig(LocalTime.parse("17:30"), LocalTime.parse("18:30")),
            new DayConfig(LocalTime.parse("19:00"), LocalTime.parse("20:00"))
        )
    }

    private static ZonedDateTime createZonedDateTime() {
        return ZonedDateTime.now()
    }
}
