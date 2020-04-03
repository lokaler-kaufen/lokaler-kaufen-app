package de.qaware.mercury.storage.login.impl

import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.login.impl.ShopLoginDataRepository
import de.qaware.mercury.storage.login.impl.ShopLoginEntity
import de.qaware.mercury.storage.shop.impl.ShopDataRepository
import de.qaware.mercury.storage.shop.impl.ShopEntity
import de.qaware.mercury.test.IntegrationTestSpecification
import de.qaware.mercury.test.fixtures.ShopEntityFixtures
import org.springframework.beans.factory.annotation.Autowired

class ShopLoginDataRepositoryIntTest extends IntegrationTestSpecification {
    @Autowired
    ShopLoginDataRepository repository
    @Autowired
    ShopDataRepository shopRepository
    @Autowired
    UUIDFactory uuidFactory
    @Autowired
    Clock clock

    def "find by email is case sensitive"() {
        given: "a shop"
        ShopEntity shopEntity = ShopEntityFixtures.create(uuidFactory, clock)
        shopRepository.save(shopEntity)

        and: "a shop login with email shop@local.host"
        ShopLoginEntity entity = new ShopLoginEntity(uuidFactory.create(), shopEntity.id, "shop@local.host", "dummy", clock.nowZoned(), clock.nowZoned())
        repository.save(entity)

        when: "we search for shop logins by SHOP@LOCAL.HOST"
        ShopLoginEntity foundEntity = repository.findFirstByEmail("SHOP@LOCAL.HOST")

        then: "we find that shop login"
        foundEntity != null
        foundEntity.id == entity.id
    }
}
