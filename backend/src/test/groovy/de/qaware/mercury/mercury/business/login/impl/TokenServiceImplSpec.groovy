package de.qaware.mercury.mercury.business.login.impl

import de.qaware.mercury.mercury.business.admin.Admin
import de.qaware.mercury.mercury.business.login.AdminToken
import de.qaware.mercury.mercury.business.login.TokenService
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
}
