package de.qaware.mercury.mercury.business.login.impl;

import de.qaware.mercury.mercury.business.admin.Admin;
import de.qaware.mercury.mercury.business.login.AdminToken;
import de.qaware.mercury.mercury.business.login.LoginException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TokenServiceImplTest {
    private TokenServiceImpl sut;

    @BeforeEach
    void setUp() {
        sut = new TokenServiceImpl();
    }

    @Test
    void name() throws LoginException {
        Admin.Id id = Admin.Id.of(UUID.randomUUID());
        AdminToken token = sut.createAdminToken(id);
        System.out.println(token);

        Admin.Id idFromToken = sut.verifyAdminToken(token);
        assertThat(idFromToken).isEqualTo(id);
    }
}