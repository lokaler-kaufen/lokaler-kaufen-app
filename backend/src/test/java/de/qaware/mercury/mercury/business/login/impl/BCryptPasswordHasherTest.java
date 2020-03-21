package de.qaware.mercury.mercury.business.login.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BCryptPasswordHasherTest {
    private BCryptPasswordHasher sut;

    @BeforeEach
    void setUp() {
        sut = new BCryptPasswordHasher();
    }

    @Test
    void name() {
        String hash = sut.hash("test");

        assertThat(sut.verify("test", hash)).isTrue();
        assertThat(sut.verify("not-test", hash)).isFalse();
    }
}