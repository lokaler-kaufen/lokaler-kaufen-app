package de.qaware.mercury.storage.admin.impl

import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.admin.impl.AdminDataRepository
import de.qaware.mercury.storage.admin.impl.AdminEntity
import de.qaware.mercury.test.IntegrationTestSpecification
import org.springframework.beans.factory.annotation.Autowired

class AdminDataRepositoryIntTest extends IntegrationTestSpecification {
    @Autowired
    AdminDataRepository repository
    @Autowired
    UUIDFactory uuidFactory
    @Autowired
    Clock clock

    def "find by email is case sensitive"() {
        given: "an admin with email admin@local.host"
        AdminEntity entity = new AdminEntity(uuidFactory.create(), "admin@local.host", "dummy", false, clock.nowZoned(), clock.nowZoned())
        repository.save(entity)

        when: "we search for admins by ADMIN@LOCAL.HOST"
        AdminEntity foundEntity = repository.findFirstByEmail("ADMIN@LOCAL.HOST")

        then: "we find that admin"
        foundEntity != null
        foundEntity.id == entity.id
    }
}
