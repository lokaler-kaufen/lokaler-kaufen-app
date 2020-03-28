package de.qaware.mercury.business.login.impl

import de.qaware.mercury.business.admin.Admin
import de.qaware.mercury.business.login.AdminToken
import de.qaware.mercury.business.login.KeyProvider
import de.qaware.mercury.business.login.PasswordResetToken
import de.qaware.mercury.business.login.ShopCreationToken
import de.qaware.mercury.business.login.ShopLogin
import de.qaware.mercury.business.login.ShopToken
import de.qaware.mercury.business.login.TokenService
import de.qaware.mercury.business.shop.Shop
import de.qaware.mercury.business.time.Clock
import spock.lang.Specification

class TokenServiceImplSpec extends Specification {

    TokenService tokenService
    KeyProvider keyProvider = Mock()
    Clock clock = Mock()

    void setup() {
        tokenService = new TokenServiceImpl(keyProvider, clock)
    }

    def "Create and Verify Admin Token"() {
        given:
        clock.nowAsLegacyDate() >> new Date()
        keyProvider.getAdminJwtSecret() >> "admin-secret"
        Admin.Id id = Admin.Id.of(UUID.randomUUID())
        AdminToken token = tokenService.createAdminToken(id)

        when:
        Admin.Id idFromToken = tokenService.verifyAdminToken(token)

        then:
        idFromToken == id
    }

    def "Create and Verify Shop Token"() {
        given:
        clock.nowAsLegacyDate() >> new Date()
        keyProvider.getShopJwtSecret() >> "shop"
        ShopLogin.Id shopLoginId = ShopLogin.Id.of(UUID.randomUUID())
        Shop.Id shopId = Shop.Id.of(UUID.randomUUID())
        ShopToken token = tokenService.createShopToken(shopLoginId, shopId)

        when:
        ShopLogin.Id idFromToken = tokenService.verifyShopToken(token)

        then:
        idFromToken == shopLoginId
    }

    def "Create and Verify Shop Creation Token"() {
        given:
        clock.nowAsLegacyDate() >> new Date()
        keyProvider.getShopCreationJwtSecret() >> "shop-creation"
        ShopCreationToken token = tokenService.createShopCreationToken("foo@bar.org")

        when:
        String email = tokenService.verifyShopCreationToken(token)

        then:
        email == "foo@bar.org"
    }

    def "Create and Verify Password Reset Token"() {
        given:
        clock.nowAsLegacyDate() >> new Date()
        keyProvider.getPasswordResetJwtSecret() >> "password-reset"
        PasswordResetToken token = tokenService.createPasswordResetToken("foo@bar.org")

        when:
        String email = tokenService.verifyPasswordResetToken(token)

        then:
        email == "foo@bar.org"
    }
}
