package de.qaware.mercury.business.login.impl

import de.qaware.mercury.mercury.business.admin.Admin
import de.qaware.mercury.mercury.business.login.*
import de.qaware.mercury.mercury.business.shop.Shop
import spock.lang.Specification

class TokenServiceImplSpec extends Specification {

    TokenService tokenService
    TokenServiceConfigurationProperties config

    def setup() {
        config = new TokenServiceConfigurationProperties("shop-secret", "admin-secret", "shop-creation-secret", "password-reset-secret")
        tokenService = new TokenServiceImpl(config)
    }

    def "Create and Verify Admin Token"() {
        given:
        Admin.Id id = Admin.Id.of(UUID.randomUUID())
        AdminToken token = tokenService.createAdminToken(id)

        when:
        Admin.Id idFromToken = tokenService.verifyAdminToken(token)

        then:
        idFromToken == id
    }

    def "Create and Verify Shop Token"() {
        given:
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
        ShopCreationToken token = tokenService.createShopCreationToken("foo@bar.org")

        when:
        String email = tokenService.verifyShopCreationToken(token)

        then:
        email == "foo@bar.org"
    }

    def "Create and Verify Password Reset Token"() {
        given:
        PasswordResetToken token = tokenService.createPasswordResetToken("foo@bar.org")

        when:
        String email = tokenService.verifyPasswordResetToken(token)

        then:
        email == "foo@bar.org"
    }
}
