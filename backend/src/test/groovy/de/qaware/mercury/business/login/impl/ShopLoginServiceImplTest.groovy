package de.qaware.mercury.business.login.impl

import de.qaware.mercury.business.email.EmailService
import de.qaware.mercury.business.login.LoginException
import de.qaware.mercury.business.login.PasswordHasher
import de.qaware.mercury.business.login.PasswordResetToken
import de.qaware.mercury.business.login.ShopLogin
import de.qaware.mercury.business.login.ShopLoginNotFoundException
import de.qaware.mercury.business.login.ShopLoginService
import de.qaware.mercury.business.login.ShopToken
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.login.VerifiedToken
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.time.Clock
import de.qaware.mercury.business.uuid.UUIDFactory
import de.qaware.mercury.storage.login.ShopLoginRepository
import de.qaware.mercury.storage.shop.ShopRepository
import de.qaware.mercury.test.time.TestClock
import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import java.time.Instant

class ShopLoginServiceImplTest extends Specification {

    ShopLoginRepository shopLoginRepository = Mock()
    PasswordHasher passwordHasher = Mock()
    TokenService tokenService = Mock()
    UUIDFactory uuidFactory = Mock()
    ShopRepository shopRepository = Mock()
    Clock clock = new TestClock()
    EmailService emailService = Mock()

    @Subject
    ShopLoginService loginService = new ShopLoginServiceImpl(shopLoginRepository, passwordHasher, tokenService, uuidFactory, shopRepository, clock, emailService)

    def "Create new Login for shop"() {
        given:
        UUID uuid = UUID.randomUUID()
        Shop shop = new Shop.ShopBuilder().id(Shop.Id.of(uuid)).name('Test Shop').build()

        when:
        ShopLogin shopLogin = loginService.createLogin(shop, 'test@lokaler.kaufen', 'geheim')

        then:
        1 * passwordHasher.hash('geheim') >> 'ahash'
        1 * uuidFactory.create() >> uuid
        1 * shopLoginRepository.insert(_)
        0 * _

        and:
        with(shopLogin) {
            id.id == uuid
            email == 'test@lokaler.kaufen'
            passwordHash == 'ahash'
        }
    }

    def "Check successful Login"() {
        given:
        ShopToken shopToken = new ShopToken('token')
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        ShopLogin.Id shopLoginId = ShopLogin.Id.of(UUID.randomUUID())
        ShopLogin shopLogin = new ShopLogin(shopLoginId, shopId, 'email', 'hash', null, null)

        when:
        VerifiedToken<ShopLogin.Id, ShopToken> verify = loginService.login('test@lokaler.kaufen', 'geheim')

        then:
        1 * shopLoginRepository.findByEmail('test@lokaler.kaufen') >> shopLogin
        1 * passwordHasher.verify('geheim', 'hash') >> true
        1 * tokenService.createShopToken(shopLoginId, shopId) >> new VerifiedToken<>(shopLogin, shopToken, Instant.now())
        verify.getToken() == shopToken
    }

    def "Verify invalid Shop Token"() {
        given:
        ShopToken token = ShopToken.of('token')
        ShopLogin.Id shopLoginId = ShopLogin.Id.of(UUID.randomUUID())

        when:
        loginService.verify(token)

        then:
        1 * tokenService.verifyShopToken(token) >> new VerifiedToken<>(shopLoginId, token, Instant.now())
        1 * shopLoginRepository.findById(shopLoginId) >> null
        0 * _
        thrown LoginException
    }

    def "Verify unknown Shop Token"() {
        given:
        ShopToken token = ShopToken.of('token')
        ShopLogin.Id shopLoginId = ShopLogin.Id.of(UUID.randomUUID())
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        ShopLogin shopLogin = new ShopLogin(shopLoginId, shopId, 'email', 'hash', null, null)

        when:
        loginService.verify(token)

        then:
        1 * tokenService.verifyShopToken(token) >> new VerifiedToken<>(shopLoginId, token, Instant.now())
        1 * shopLoginRepository.findById(shopLoginId) >> shopLogin
        1 * shopRepository.findById(shopId) >> null
        0 * _
        thrown LoginException
    }

    def "Verify valid Shop Token"() {
        given:
        ShopToken token = ShopToken.of('token')
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        Shop shop = new Shop.ShopBuilder().id(shopId).build()
        ShopLogin.Id shopLoginId = ShopLogin.Id.of(UUID.randomUUID())
        ShopLogin shopLogin = new ShopLogin(shopLoginId, shopId, 'email', 'hash', null, null)

        when:
        Shop verify = loginService.verify(token)

        then:
        1 * tokenService.verifyShopToken(token) >> new VerifiedToken<>(shopLoginId, token, Instant.now())
        1 * shopLoginRepository.findById(shopLoginId) >> shopLogin
        1 * shopRepository.findById(shopId) >> shop
        0 * _
        verify == shop
    }

    def "Send password reset link for known email"() {
        given:
        ShopLogin shopLogin = new ShopLogin(null, null, null, null, null, null)
        PasswordResetToken token = PasswordResetToken.of('token')

        when:
        loginService.sendPasswordResetLink('known@lokaler.kaufen')

        then:
        1 * shopLoginRepository.findByEmail('known@lokaler.kaufen') >> shopLogin
        1 * tokenService.createPasswordResetToken('known@lokaler.kaufen') >> token
        1 * emailService.sendShopPasswordResetEmail('known@lokaler.kaufen', token)
        0 * _
    }

    def "Don't send password reset link for unknown email"() {
        when:
        loginService.sendPasswordResetLink('unknown@lokaler.kaufen')

        then:
        1 * shopLoginRepository.findByEmail('unknown@lokaler.kaufen') >> null
        0 * emailService._
        0 * tokenService._
    }

    def "Reset Password for known Email"() {
        given:
        ShopLogin login = new ShopLogin(null, null, null, null, null, null)

        when:
        loginService.resetPassword('known@lokaler.kaufen', 'secret')

        then:
        1 * shopLoginRepository.findByEmail('known@lokaler.kaufen') >> login
        1 * passwordHasher.hash('secret') >> 'ahash'
        1 * shopLoginRepository.update({ ShopLogin l -> l.passwordHash == 'ahash' })
        0 * _
    }

    def "Reset Password for unknown Email"() {
        when:
        loginService.resetPassword('unknown@lokaler.kaufen', 'secret')

        then:
        1 * shopLoginRepository.findByEmail('unknown@lokaler.kaufen') >> null
        0 * _
        thrown ShopLoginNotFoundException
    }

    @Unroll
    def "Check login for #email"() {
        when:
        boolean hasLogin = loginService.hasLogin(email)

        then:
        1 * shopLoginRepository.findByEmail(email) >> login
        0 * _
        hasLogin == expected

        where:
        email                    | login                                             || expected
        'known@lokaler.kaufen'   | new ShopLogin(null, null, null, null, null, null) || true
        'unknown@lokaler.kaufen' | null                                              || false
    }
}
