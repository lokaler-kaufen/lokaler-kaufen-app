package de.qaware.mercury.business.login.impl

import de.qaware.mercury.business.admin.Admin
import de.qaware.mercury.business.login.AdminEmailSettings
import de.qaware.mercury.business.login.AdminLoginService
import de.qaware.mercury.business.login.AdminToken
import de.qaware.mercury.business.login.LoginException
import de.qaware.mercury.business.login.PasswordHasher
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.login.TokenWithExpiry
import de.qaware.mercury.business.login.VerifiedToken
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.admin.AdminRepository
import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import spock.lang.Unroll

import java.time.Instant
import java.time.ZonedDateTime

@ContextConfiguration(classes = AdminLoginServiceImpl)
class AdminLoginServiceImplSpec extends Specification {

    @Autowired
    AdminLoginService loginService

    @SpringBean
    AdminRepository adminRepository = Mock()
    @SpringBean
    PasswordHasher passwordHasher = Mock()
    @SpringBean
    TokenService tokenService = Mock()
    @SpringBean
    UUIDFactory uuidFactory = Mock()
    @SpringBean
    Clock clock = Mock()

    def "Create Login for Admin"() {
        given:
        UUID uuid = UUID.randomUUID()
        String hasher = '4711'

        when:
        Admin admin = loginService.createLogin('test@lokaler.kaufen', 'supersecret', AdminEmailSettings.noEmails())

        then:
        1 * uuidFactory.create() >> uuid
        1 * passwordHasher.hash('supersecret') >> hasher
        2 * clock.nowZoned() >> ZonedDateTime.now()
        1 * adminRepository.insert(_)

        with(admin) {
            id.id == uuid
            email == 'test@lokaler.kaufen'
            passwordHash == hasher
        }
    }

    def "Successful login for Admin"() {
        given:
        Admin.Id id = Admin.Id.of(UUID.randomUUID())
        Admin admin = new Admin(id, 'test@lokaler.kaufen', '4711', false, ZonedDateTime.now(), ZonedDateTime.now())
        AdminToken token = AdminToken.of('token')

        when:
        TokenWithExpiry<AdminToken> adminToken = loginService.login('test@lokaler.kaufen', 'supersecret')

        then:
        1 * adminRepository.findByEmail('test@lokaler.kaufen') >> admin
        1 * passwordHasher.verify('supersecret', '4711') >> true
        1 * tokenService.createAdminToken(id) >> new TokenWithExpiry(token, ZonedDateTime.now())

        adminToken.getToken() == token
    }

    @Unroll
    def "Unsuccessful login for Admin #admin"() {
        setup:
        passwordHasher.verify('supersecret', '4711') >> false

        when:
        loginService.login('test@lokaler.kaufen', 'supersecret')

        then:
        1 * adminRepository.findByEmail('test@lokaler.kaufen') >> admin
        thrown LoginException

        where:
        admin << [null, new Admin(Admin.Id.of(UUID.randomUUID()), 'test@lokaler.kaufen', '4711', false, null, null)]
    }

    def "Successful AdminToken Verification"() {
        given:
        Admin.Id id = Admin.Id.of(UUID.randomUUID())
        Admin admin = new Admin(id, 'test@lokaler.kaufen', '4711', false, ZonedDateTime.now(), ZonedDateTime.now())
        AdminToken token = AdminToken.of('token')

        when:
        Admin verified = loginService.verify(token)

        then:
        1 * tokenService.verifyAdminToken(token) >> new VerifiedToken<>(id, Instant.now())
        1 * adminRepository.findById(id) >> admin
        verified == admin
    }

    def "AdminToken Verification Error"() {
        given:
        Admin.Id id = Admin.Id.of(UUID.randomUUID())
        AdminToken token = AdminToken.of('token')

        when:
        loginService.verify(token)

        then:
        1 * tokenService.verifyAdminToken(token) >> new VerifiedToken<>(id, Instant.now())
        1 * adminRepository.findById(id) >> null
        thrown LoginException
    }

    @Unroll
    def "Find Admin by Email #email"() {
        when:
        Admin found = loginService.findByEmail(email)

        then:
        1 * adminRepository.findByEmail(email) >> admin
        found == admin

        where:
        email                     | admin
        'notfound@lokaler.kaufen' | null
        'test@lokaler.kaufen' | new Admin(Admin.Id.of(UUID.randomUUID()), 'test@lokaler.kaufen', '4711', false, null, null)
    }
}
